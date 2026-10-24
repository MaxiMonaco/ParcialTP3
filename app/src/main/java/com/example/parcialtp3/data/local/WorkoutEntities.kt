package com.example.parcialtp3.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

/** Fila del catalogo en cache (GET /v1/workouts). `position` conserva el orden que mando la API. */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val name: String,
    val level: String,
    val durationMinutes: Int?,
    val exerciseCount: Int?,
    val position: Int,
)

/**
 * Detalle en cache (GET /v1/workouts/{id}). Que exista esta fila significa "este detalle ya se
 * descargo": es lo que permite verlo sin conexion y avisar cuando nunca se descargo (RF02).
 */
@Entity(tableName = "workout_details")
data class WorkoutDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val level: String,
    val durationMinutes: Int?,
    val cachedAt: Long,
)

/** Ejercicio de un detalle. El id lleva el prefijo del entrenamiento para no chocar entre entrenamientos. */
@Entity(tableName = "exercises", indices = [Index(value = ["workoutId"])])
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val workoutId: String,
    val position: Int,
    val name: String,
    val sets: Int,
    val reps: String,
    val restSeconds: Int?,
    val instructions: String, // un paso por linea
)

/** Resultado de una consulta con relacion: el detalle y sus ejercicios (Room los une solo). */
data class WorkoutWithExercises(
    @Embedded val workout: WorkoutDetailEntity,
    @Relation(parentColumn = "id", entityColumn = "workoutId")
    val exercises: List<ExerciseEntity>,
)
