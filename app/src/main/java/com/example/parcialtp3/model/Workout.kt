package com.example.parcialtp3.model

import com.example.parcialtp3.data.local.ExerciseEntity
import com.example.parcialtp3.data.local.WorkoutDetailEntity
import com.example.parcialtp3.data.local.WorkoutEntity
import com.example.parcialtp3.data.local.WorkoutWithExercises
import com.example.parcialtp3.data.remote.ExerciseDto
import com.example.parcialtp3.data.remote.WorkoutDto
import com.example.parcialtp3.data.remote.asIntOrNull
import com.example.parcialtp3.data.remote.asTextListOrEmpty
import com.example.parcialtp3.data.remote.asTextOrNull

/**
 * Modelos que ven las pantallas y los ViewModels. Cada capa de abajo tiene el suyo
 * (DTO para la red, Entity para Room) y los mappers de este archivo los convierten (RF06).
 */
data class Workout(
    val id: String,
    val name: String,
    val level: String,
    val durationMinutes: Int?,
    val exerciseCount: Int?,
)

data class Exercise(
    val id: String,
    val workoutId: String,
    val position: Int,
    val name: String,
    val sets: Int,
    val reps: String,
    val restSeconds: Int?,
    val instructions: List<String>,
)

data class WorkoutDetail(
    val id: String,
    val name: String,
    val level: String,
    val durationMinutes: Int?,
    val exercises: List<Exercise>,
)

// ---------- Red -> Room ----------

/** Un item del catalogo sin id no se puede abrir ni guardar: devuelve null y se descarta. */
fun WorkoutDto.toCatalogEntity(position: Int): WorkoutEntity? {
    val workoutId = id?.trim().orEmpty()
    if (workoutId.isEmpty()) return null
    return WorkoutEntity(
        id = workoutId,
        name = name?.trim().orEmpty().ifEmpty { workoutId },
        level = level?.trim().orEmpty(),
        durationMinutes = durationMinutes,
        exerciseCount = exerciseCount,
        position = position,
    )
}

fun WorkoutDto.toDetailEntity(fallbackId: String, now: Long): WorkoutDetailEntity {
    val workoutId = id?.trim().orEmpty().ifEmpty { fallbackId }
    return WorkoutDetailEntity(
        id = workoutId,
        name = name?.trim().orEmpty().ifEmpty { workoutId },
        level = level?.trim().orEmpty(),
        durationMinutes = durationMinutes,
        cachedAt = now,
    )
}

/**
 * El id del ejercicio se arma con el del entrenamiento + el de la API (o su posicion): asi dos
 * entrenamientos con ejercicios "1", "2", "3" no se pisan en la tabla.
 */
fun ExerciseDto.toEntity(workoutId: String, position: Int): ExerciseEntity =
    ExerciseEntity(
        id = "$workoutId:${id?.trim().orEmpty().ifEmpty { position.toString() }}",
        workoutId = workoutId,
        position = position,
        name = name?.trim().orEmpty().ifEmpty { "Exercise ${position + 1}" },
        sets = (sets.asIntOrNull() ?: 1).coerceAtLeast(1),
        reps = reps.asTextOrNull() ?: "-",
        restSeconds = restSeconds.asIntOrNull(),
        instructions = instructions.asTextListOrEmpty().joinToString("\n"),
    )

// ---------- Room -> app ----------

fun WorkoutEntity.toWorkout() = Workout(
    id = id,
    name = name,
    level = level,
    durationMinutes = durationMinutes,
    exerciseCount = exerciseCount,
)

fun ExerciseEntity.toExercise() = Exercise(
    id = id,
    workoutId = workoutId,
    position = position,
    name = name,
    sets = sets,
    reps = reps,
    restSeconds = restSeconds,
    instructions = instructions.lines().filter { it.isNotBlank() },
)

fun WorkoutWithExercises.toDetail() = WorkoutDetail(
    id = workout.id,
    name = workout.name,
    level = workout.level,
    durationMinutes = workout.durationMinutes,
    exercises = exercises.sortedBy { it.position }.map { it.toExercise() },
)
