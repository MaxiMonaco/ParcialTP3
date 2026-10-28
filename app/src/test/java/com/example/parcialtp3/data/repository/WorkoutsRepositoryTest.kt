package com.example.parcialtp3.data.repository

import com.example.parcialtp3.data.local.ExerciseEntity
import com.example.parcialtp3.data.local.WorkoutDao
import com.example.parcialtp3.data.local.WorkoutDetailEntity
import com.example.parcialtp3.data.local.WorkoutEntity
import com.example.parcialtp3.data.local.WorkoutWithExercises
import com.example.parcialtp3.data.remote.ApiResponse
import com.example.parcialtp3.data.remote.WorkoutDto
import com.example.parcialtp3.data.remote.WorkoutsApi
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Pruebas de la regla central del repositorio (RF02 y RF05): la API solo reemplaza la cache de
 * Room cuando responde bien; ante cualquier fallo (sin conexion, 503, JSON invalido, respuesta sin
 * "data") la cache no se toca. Corren en la JVM, sin emulador: ./gradlew :app:testDebugUnitTest
 *
 * Se usan dobles de prueba escritos a mano (sin Mockito) para que el test se lea como la regla.
 */
class WorkoutsRepositoryTest {

    // ---------- Dobles de prueba ----------

    /** API falsa: devuelve lo que se le configure o lanza la excepcion indicada. */
    private class FakeApi(
        private val catalog: () -> ApiResponse<List<WorkoutDto>>,
        private val detail: (String) -> ApiResponse<WorkoutDto> = { error("no configurado") },
    ) : WorkoutsApi {
        override suspend fun getWorkouts(level: String?) = catalog()
        override suspend fun getWorkout(workoutId: String) = detail(workoutId)
    }

    /** DAO falso: solo cuenta cuantas veces se intento escribir en la cache. */
    private class FakeDao : WorkoutDao() {
        var catalogWrites = 0
        var detailWrites = 0
        var lastCatalog: List<WorkoutEntity> = emptyList()

        override suspend fun replaceCatalog(items: List<WorkoutEntity>) {
            catalogWrites++
            lastCatalog = items
        }

        override suspend fun saveDetail(detail: WorkoutDetailEntity, exercises: List<ExerciseEntity>) {
            detailWrites++
        }

        override fun observeCatalog(): Flow<List<WorkoutEntity>> = flowOf(lastCatalog)
        override suspend fun insertCatalog(items: List<WorkoutEntity>) = Unit
        override suspend fun clearCatalog() = Unit
        override fun observeDetail(workoutId: String): Flow<WorkoutWithExercises?> = flowOf(null)
        override suspend fun insertDetail(detail: WorkoutDetailEntity) = Unit
        override suspend fun insertExercises(exercises: List<ExerciseEntity>) = Unit
        override suspend fun deleteExercises(workoutId: String) = Unit
        override fun observeExercise(exerciseId: String): Flow<ExerciseEntity?> = flowOf(null)
    }

    private fun dto(id: String, name: String = "Workout $id") =
        WorkoutDto(id = id, name = name, level = "beginner", durationMinutes = 20, exerciseCount = 3, exercises = null)

    private fun http503(): HttpException =
        HttpException(Response.error<Any>(503, "".toResponseBody("application/json".toMediaType())))

    // ---------- Respuesta valida ----------

    @Test
    fun catalogoValidoReemplazaLaCache() = runBlocking {
        val dao = FakeDao()
        val repo = WorkoutsRepository(FakeApi({ ApiResponse(listOf(dto("w01"), dto("w02"))) }), dao)

        val result = repo.refreshCatalog()

        assertTrue(result.isSuccess)
        assertEquals(1, dao.catalogWrites)
        assertEquals(listOf("w01", "w02"), dao.lastCatalog.map { it.id })
    }

    @Test
    fun catalogoConservaElOrdenDeLaApi() = runBlocking {
        val dao = FakeDao()
        val repo = WorkoutsRepository(FakeApi({ ApiResponse(listOf(dto("w03"), dto("w01"))) }), dao)

        repo.refreshCatalog()

        assertEquals(listOf(0, 1), dao.lastCatalog.map { it.position })
        assertEquals("w03", dao.lastCatalog.first().id)
    }

    @Test
    fun itemSinIdSeDescartaSinRomperElResto() = runBlocking {
        val dao = FakeDao()
        val repo = WorkoutsRepository(FakeApi({ ApiResponse(listOf(dto("w01"), dto(id = ""), dto("w02"))) }), dao)

        repo.refreshCatalog()

        assertEquals(listOf("w01", "w02"), dao.lastCatalog.map { it.id })
    }

    // ---------- Fallos: la cache no se toca (RF05) ----------

    @Test
    fun sinConexionNoTocaLaCache() = runBlocking {
        val dao = FakeDao()
        val repo = WorkoutsRepository(FakeApi({ throw IOException("sin red") }), dao)

        val result = repo.refreshCatalog()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.isOffline())
        assertEquals(0, dao.catalogWrites)
    }

    @Test
    fun error503NoTocaLaCache() = runBlocking {
        val dao = FakeDao()
        val repo = WorkoutsRepository(FakeApi({ throw http503() }), dao)

        val result = repo.refreshCatalog()

        assertTrue(result.isFailure)
        assertEquals("El servidor respondió con un error (503).", result.exceptionOrNull()!!.toUserMessage())
        assertEquals(0, dao.catalogWrites)
    }

    @Test
    fun jsonInvalidoNoTocaLaCache() = runBlocking {
        val dao = FakeDao()
        val repo = WorkoutsRepository(FakeApi({ throw JsonSyntaxException("malformed") }), dao)

        val result = repo.refreshCatalog()

        assertTrue(result.isFailure)
        assertEquals("La respuesta del servidor no es válida.", result.exceptionOrNull()!!.toUserMessage())
        assertEquals(0, dao.catalogWrites)
    }

    @Test
    fun respuestaSinDataNoTocaLaCache() = runBlocking {
        val dao = FakeDao()
        val repo = WorkoutsRepository(FakeApi({ ApiResponse(null) }), dao)

        val result = repo.refreshCatalog()

        assertTrue(result.isFailure)
        assertEquals(0, dao.catalogWrites)
    }

    @Test
    fun detalle404NoTocaLaCache() = runBlocking {
        val dao = FakeDao()
        val api = FakeApi(
            catalog = { ApiResponse(emptyList()) },
            detail = { throw HttpException(Response.error<Any>(404, "".toResponseBody("application/json".toMediaType()))) },
        )
        val repo = WorkoutsRepository(api, dao)

        val result = repo.refreshDetail("no-existe")

        assertTrue(result.isFailure)
        assertEquals("El recurso no existe (404).", result.exceptionOrNull()!!.toUserMessage())
        assertEquals(0, dao.detailWrites)
    }

    @Test
    fun detalleValidoGuardaEnRoom() = runBlocking {
        val dao = FakeDao()
        val api = FakeApi(catalog = { ApiResponse(emptyList()) }, detail = { id -> ApiResponse(dto(id)) })
        val repo = WorkoutsRepository(api, dao)

        val result = repo.refreshDetail("w01")

        assertTrue(result.isSuccess)
        assertEquals(1, dao.detailWrites)
    }
}
