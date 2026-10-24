package com.example.parcialtp3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.data.repository.MealsRepository
import com.example.parcialtp3.model.DayTotals
import com.example.parcialtp3.model.Meal
import com.example.parcialtp3.model.MealDates
import com.example.parcialtp3.model.totals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Las comidas de UN dia con su total diario. `isLoading` solo es true hasta que Room responde. */
data class MealsUiState(
    val date: String,
    val isToday: Boolean,
    val meals: List<Meal>,
    val totals: DayTotals,
    val isLoading: Boolean,
)

/**
 * Listado por fecha (RF04). Empieza en hoy; previousDay/nextDay cambian el dia. Como el DAO
 * devuelve un Flow, al agregar o eliminar una comida el listado y el total cambian solos.
 */
@HiltViewModel
class MealsViewModel @Inject constructor(
    repository: MealsRepository,
) : ViewModel() {

    private val selectedDate = MutableStateFlow(MealDates.today())

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MealsUiState> = selectedDate
        .flatMapLatest { date ->
            repository.observeByDate(date).map { meals ->
                MealsUiState(
                    date = date,
                    isToday = date == MealDates.today(),
                    meals = meals,
                    totals = meals.totals(),
                    isLoading = false,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MealsUiState(
                date = selectedDate.value,
                isToday = true,
                meals = emptyList(),
                totals = DayTotals(),
                isLoading = true,
            ),
        )

    fun previousDay() {
        selectedDate.value = MealDates.shift(selectedDate.value, -1)
    }

    fun nextDay() {
        selectedDate.value = MealDates.shift(selectedDate.value, 1)
    }
}
