package com.example.parcialtp3.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Endpoints obligatorios de la VORA Mock API (consigna, diapositiva 8).
 * La baseUrl (https://tp3.martinrivas.ar/) esta en NetworkModule; aca va solo el resto del camino.
 */
interface WorkoutsApi {

    // Catalogo: 3 entrenamientos. `level` es un filtro opcional (null = no se envia).
    @GET("v1/workouts")
    suspend fun getWorkouts(@Query("level") level: String? = null): ApiResponse<List<WorkoutDto>>

    // Detalle con ejercicios. Si el id no existe responde 404 y Retrofit lanza HttpException.
    @GET("v1/workouts/{workoutId}")
    suspend fun getWorkout(@Path("workoutId") workoutId: String): ApiResponse<WorkoutDto>
}
