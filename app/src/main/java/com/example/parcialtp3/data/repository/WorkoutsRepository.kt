package com.example.parcialtp3.data.repository

import com.example.parcialtp3.data.local.WorkoutDao
import com.example.parcialtp3.data.remote.WorkoutsApi
import com.example.parcialtp3.model.Exercise
import com.example.parcialtp3.model.Workout
import com.example.parcialtp3.model.WorkoutDetail
import com.example.parcialtp3.model.toCatalogEntity
import com.example.parcialtp3.model.toDetail
import com.example.parcialtp3.model.toDetailEntity
import com.example.parcialtp3.model.toEntity
import com.example.parcialtp3.model.toExercise
import com.example.parcialtp3.model.toWorkout
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Unico lugar que coordina la API (Retrofit) y la cache (Room) de entrenamientos (RF06).
 *
 * Regla: la UI SIEMPRE lee de Room (los metodos observe*), y los metodos refresh* piden a la API y,
 * solo si la respuesta es valida, reemplazan la cache. Si la red falla, Room no se toca: la ultima
 * cache valida sigue ahi (RF05) y es lo que permite ver un detalle ya descargado sin conexion (RF02).
 */
class WorkoutsRepository @Inject constructor(
    private val api: WorkoutsApi,
    private val dao: WorkoutDao,
) {

    val catalog: Flow<List<Workout>> =
        dao.observeCatalog().map { list -> list.map { it.toWorkout() } }

    /** null = ese detalle nunca se descargo. */
    fun observeDetail(workoutId: String): Flow<WorkoutDetail?> =
        dao.observeDetail(workoutId).map { it?.toDetail() }

    fun observeExercise(exerciseId: String): Flow<Exercise?> =
        dao.observeExercise(exerciseId).map { it?.toExercise() }

    /** GET /v1/workouts -> reemplaza el catalogo en Room. Un fallo no borra nada. */
    suspend fun refreshCatalog(): Result<Unit> =
        try {
            val items = api.getWorkouts().data ?: error("La respuesta no trae el campo data")
            val entities = items.mapIndexedNotNull { index, dto -> dto.toCatalogEntity(index) }
            dao.replaceCatalog(entities)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e // si se cancela la coroutine, hay que dejarla cancelar
        } catch (e: Exception) {
            Result.failure(e)
        }

    /** GET /v1/workouts/{id} -> guarda el detalle y sus ejercicios en Room. */
    suspend fun refreshDetail(workoutId: String): Result<Unit> =
        try {
            val dto = api.getWorkout(workoutId).data ?: error("La respuesta no trae el campo data")
            val detail = dto.toDetailEntity(fallbackId = workoutId, now = System.currentTimeMillis())
            val exercises = dto.exercises.orEmpty()
                .mapIndexed { index, exercise -> exercise.toEntity(detail.id, index) }
            dao.saveDetail(detail, exercises)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
