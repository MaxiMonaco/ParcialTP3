package com.example.parcialtp3.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.data.repository.WorkoutsRepository
import com.example.parcialtp3.model.Exercise
import com.example.parcialtp3.navigation.NavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface ExerciseUiState {
    data object Loading : ExerciseUiState
    data object NotFound : ExerciseUiState
    data class Content(val exercise: Exercise) : ExerciseUiState
}

/**
 * La guia de un ejercicio sale de la cache de Room: el ejercicio se guardo cuando se descargo el
 * detalle de su entrenamiento (de ahi se llega a esta pantalla), por eso funciona sin conexion.
 */
@HiltViewModel
class ExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: WorkoutsRepository,
) : ViewModel() {

    private val exerciseId: String = savedStateHandle.get<String>(NavArgs.EXERCISE_ID).orEmpty()

    val uiState: StateFlow<ExerciseUiState> = repository.observeExercise(exerciseId)
        .map { exercise -> if (exercise == null) ExerciseUiState.NotFound else ExerciseUiState.Content(exercise) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ExerciseUiState.Loading,
        )
}
