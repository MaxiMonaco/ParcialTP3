package com.example.parcialtp3.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Una comida registrada a mano (RF03). `date` va como texto "yyyy-MM-dd" (ordena bien y se filtra
 * con =) y `type` guarda el nombre del enum MealType (BREAKFAST, LUNCH, SNACK, DINNER).
 */
@Entity(tableName = "meals", indices = [Index(value = ["date"])])
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // 0 = "todavia sin id": Room asigna uno
    val name: String,
    val type: String,
    val date: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val createdAt: Long,
)
