package com.example.parcialtp3.data.repository

import com.example.parcialtp3.data.local.MealDao
import com.example.parcialtp3.model.Meal
import com.example.parcialtp3.model.toEntity
import com.example.parcialtp3.model.toMeal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Unico lugar que habla con Room para las comidas. Las pantallas y ViewModels nunca tocan el DAO. */
class MealsRepository @Inject constructor(
    private val dao: MealDao,
) {

    fun observeByDate(date: String): Flow<List<Meal>> =
        dao.observeByDate(date).map { list -> list.map { it.toMeal() } }

    /** null = la comida no existe (por ejemplo, ya se elimino). */
    fun observeMeal(id: Long): Flow<Meal?> =
        dao.observeById(id).map { it?.toMeal() }

    suspend fun add(meal: Meal) {
        dao.insert(meal.toEntity())
    }

    suspend fun delete(id: Long) = dao.deleteById(id)
}
