package com.example.parcialtp3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Punto de entrada de Room. Lista las entidades (tablas) y expone los DAOs.
 * Room genera la implementacion (AppDatabase_Impl) en tiempo de compilacion con KSP.
 */
@Database(
    entities = [
        WorkoutEntity::class,       // catalogo de entrenamientos en cache
        WorkoutDetailEntity::class, // detalle de entrenamiento en cache
        ExerciseEntity::class,      // ejercicios de cada detalle
        MealEntity::class,          // comidas registradas a mano
    ],
    version = 3,          // sube cuando cambia el esquema (v3: se quito la tabla de frases del TP anterior)
    exportSchema = false, // no exporta el JSON del esquema; en el parcial no hace falta
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun mealDao(): MealDao
}
