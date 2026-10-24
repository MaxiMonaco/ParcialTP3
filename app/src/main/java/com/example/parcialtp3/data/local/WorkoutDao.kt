package com.example.parcialtp3.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a la cache de entrenamientos. Es una clase abstracta (y no una interfaz) porque Room
 * solo permite metodos con cuerpo, como las transacciones de abajo, en clases abstractas.
 */
@Dao
abstract class WorkoutDao {

    // ---------- Catalogo ----------

    @Query("SELECT * FROM workouts ORDER BY position ASC")
    abstract fun observeCatalog(): Flow<List<WorkoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCatalog(items: List<WorkoutEntity>)

    @Query("DELETE FROM workouts")
    abstract suspend fun clearCatalog()

    // Borrar e insertar juntos: o se aplica todo o no se aplica nada.
    @Transaction
    open suspend fun replaceCatalog(items: List<WorkoutEntity>) {
        clearCatalog()
        insertCatalog(items)
    }

    // ---------- Detalle ----------

    @Transaction
    @Query("SELECT * FROM workout_details WHERE id = :workoutId")
    abstract fun observeDetail(workoutId: String): Flow<WorkoutWithExercises?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertDetail(detail: WorkoutDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Query("DELETE FROM exercises WHERE workoutId = :workoutId")
    abstract suspend fun deleteExercises(workoutId: String)

    @Transaction
    open suspend fun saveDetail(detail: WorkoutDetailEntity, exercises: List<ExerciseEntity>) {
        deleteExercises(detail.id)
        insertDetail(detail)
        insertExercises(exercises)
    }

    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    abstract fun observeExercise(exerciseId: String): Flow<ExerciseEntity?>
}
