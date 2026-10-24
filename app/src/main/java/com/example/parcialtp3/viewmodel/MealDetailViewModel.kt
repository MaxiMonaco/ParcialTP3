package com.example.parcialtp3.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.data.repository.MealsRepository
import com.example.parcialtp3.model.Meal
import com.example.parcialtp3.navigation.NavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MealDetailUiState {
    data object Loading : MealDetailUiState
    data object NotFound : MealDetailUiState
    data class Content(val meal: Meal) : MealDetailUiState
}

@HiltViewModel
class MealDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MealsRepository,
) : ViewModel() {

    // El id viaja como texto en la ruta; -1 no existe nunca (los ids de Room empiezan en 1).
    private val mealId: Long = savedStateHandle.get<String>(NavArgs.MEAL_ID)?.toLongOrNull() ?: -1L

    val uiState: StateFlow<MealDetailUiState> = repository.observeMeal(mealId)
        .map { meal -> if (meal == null) MealDetailUiState.NotFound else MealDetailUiState.Content(meal) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MealDetailUiState.Loading,
        )

    /** Elimina la comida (la pantalla ya pidio confirmacion) y avisa para volver atras. */
    fun delete(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.delete(mealId)
            onDeleted()
        }
    }
}
