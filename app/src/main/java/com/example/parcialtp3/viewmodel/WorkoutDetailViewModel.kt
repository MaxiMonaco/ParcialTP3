package com.example.parcialtp3.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.data.repository.WorkoutsRepository
import com.example.parcialtp3.data.repository.isOffline
import com.example.parcialtp3.data.repository.toUserMessage
import com.example.parcialtp3.model.WorkoutDetail
import com.example.parcialtp3.navigation.NavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Estados del detalle de un entrenamiento (RF01, RF02, RF05). */
sealed interface WorkoutDetailUiState {
    data object Loading : WorkoutDetailUiState
    data class Error(val message: String) : WorkoutDetailUiState
    data class Content(val workout: WorkoutDetail, val warning: String? = null) : WorkoutDetailUiState
}

private sealed interface DetailRefresh {
    data object Loading : DetailRefresh
    data object Done : DetailRefresh
    data class Failed(val error: Throwable) : DetailRefresh
}

/**
 * Hilt arma este ViewModel y le pasa el SavedStateHandle con los argumentos de la ruta: por eso
 * `workoutId` llega solo (la clave es la misma de la ruta, NavArgs.WORKOUT_ID). Cada entrenamiento
 * abre SU detalle (NAV04).
 */
@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WorkoutsRepository,
) : ViewModel() {

    private val workoutId: String = savedStateHandle.get<String>(NavArgs.WORKOUT_ID).orEmpty()

    private val refresh = MutableStateFlow<DetailRefresh>(DetailRefresh.Loading)

    val uiState: StateFlow<WorkoutDetailUiState> = combine(repository.observeDetail(workoutId), refresh) { detail, state ->
        when {
            // Hay cache: se muestra siempre. Si la actualizacion fallo, se agrega un aviso.
            detail != null -> WorkoutDetailUiState.Content(
                workout = detail,
                warning = (state as? DetailRefresh.Failed)?.let {
                    if (it.error.isOffline()) "Sin conexión: mostrando los datos guardados."
                    else "No se pudo actualizar: ${it.error.toUserMessage()} Mostrando datos guardados."
                },
            )
            state is DetailRefresh.Loading -> WorkoutDetailUiState.Loading
            // Nunca se descargo y no se pudo descargar ahora.
            state is DetailRefresh.Failed -> WorkoutDetailUiState.Error(
                if (state.error.isOffline()) "Este entrenamiento no está disponible sin conexión."
                else state.error.toUserMessage(),
            )
            else -> WorkoutDetailUiState.Error("Este entrenamiento no está disponible.")
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WorkoutDetailUiState.Loading,
    )

    init {
        load()
    }

    fun load() {
        refresh.value = DetailRefresh.Loading
        viewModelScope.launch {
            repository.refreshDetail(workoutId)
                .onSuccess { refresh.value = DetailRefresh.Done }
                .onFailure { refresh.value = DetailRefresh.Failed(it) }
        }
    }

    fun retry() = load()
}
