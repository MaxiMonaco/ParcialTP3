package com.example.parcialtp3.data.remote

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

/**
 * Un entrenamiento tal como lo manda la API. Gson lee los campos POR NOMBRE, y las clases de este
 * paquete estan protegidas de R8 en app/src/main/keepRules/app-rules.keep.
 *
 * Catalogo (GET /v1/workouts) confirmado con la respuesta real:
 *   { "id": "w01", "name": "Fuerza inicial", "level": "beginner", "durationMinutes": 25, "exerciseCount": 3 }
 * Detalle (GET /v1/workouts/{id}) agrega la lista de ejercicios.
 */
data class WorkoutDto(
    val id: String?,
    val name: String?,
    val level: String?,
    val durationMinutes: Int?,
    val exerciseCount: Int?,
    val exercises: List<ExerciseDto>?,
)

/**
 * Ejercicio del detalle. PROVISORIO: los nombres de campo se tomaron de la consigna (series,
 * repeticiones, instrucciones) y se aceptan variantes con `alternate`. Los valores van como
 * JsonElement para tolerar numeros o textos ("12" u "8-12"). Ajustar cuando se confirme el JSON
 * real de GET /v1/workouts/w01.
 */
data class ExerciseDto(
    @SerializedName(value = "id", alternate = ["exerciseId", "exercise_id"])
    val id: String?,
    @SerializedName(value = "name", alternate = ["title"])
    val name: String?,
    @SerializedName(value = "sets", alternate = ["series"])
    val sets: JsonElement?,
    @SerializedName(value = "reps", alternate = ["repetitions", "repeticiones"])
    val reps: JsonElement?,
    @SerializedName(value = "restSeconds", alternate = ["rest_seconds", "rest"])
    val restSeconds: JsonElement?,
    @SerializedName(value = "instructions", alternate = ["steps", "description"])
    val instructions: JsonElement?,
)
