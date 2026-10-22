package com.example.parcialtp3.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert
    suspend fun insert(meal: MealEntity): Long

    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteById(id: Long)

    // Flow: se vuelve a emitir solo cada vez que la tabla cambia, asi el listado y el total
    // diario se actualizan al agregar o eliminar sin pedir nada de nuevo (RF04).
    @Query("SELECT * FROM meals WHERE date = :date ORDER BY createdAt ASC")
    fun observeByDate(date: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE id = :id")
    fun observeById(id: Long): Flow<MealEntity?>
}
