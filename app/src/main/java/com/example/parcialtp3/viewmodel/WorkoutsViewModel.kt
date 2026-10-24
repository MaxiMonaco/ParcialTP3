package com.example.parcialtp3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.data.repository.WorkoutsRepository
import com.example.parcialtp3.data.repository.toUserMessage
import com.example.parcialtp3.model.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estados de la pantalla del catalogo (RF05): carga, contenido, vacio y error con reintento.
 * `Content.warning` aparece cuando la actualizacion fallo pero hay cache: se muestra la cache y
 * un aviso, nunca se borra (RF05).
 */
sealed interface WorkoutsUiState {
    data object Loading : WorkoutsUiState
    data object Empty : WorkoutsUiState
    data class Error(val message: String) : WorkoutsUiState
    data class Content(
        val workouts: List<Workout>,
        val isRefreshing: Boolean = false,
        val warning: String? = null,
    ) : WorkoutsUiState
}

// Estado de la ultima consulta a la API (independiente de lo que haya en Room).
private sealed interface RefreshState {
    data object Loading : RefreshState
    data object Done : RefreshState
    data class Failed(val message: String) : RefreshState
}

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val repository: WorkoutsRepository,
) : ViewModel() {

    private val refreshState = MutableStateFlow<RefreshState>(RefreshState.Loading)

    // Une "lo que hay en Room" con "como le fue a la ultima consulta". La rotacion no repite
    // la consulta: el ViewModel y este StateFlow sobreviven (RF07).
    val uiState: StateFlow<WorkoutsUiState> = combine(repository.catalog, refreshState) { workouts, refresh ->
        when {
            workouts.isNotEmpty() -> WorkoutsUiState.Content(
                workouts = workouts,
                isRefreshing = refresh is RefreshState.Loading,
                warning = (refresh as? RefreshState.Failed)?.let { "No se pudo actualizar: ${it.message} Mostrando datos guardados." },
            )
            refresh is RefreshState.Loading -> WorkoutsUiState.Loading
            refresh is RefreshState.Failed -> WorkoutsUiState.Error(refresh.message)
            else -> WorkoutsUiState.Empty
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WorkoutsUiState.Loading,
    )

    init {
        load() // "Al entrar pide el catalogo"
    }

    // Lo llaman la carga inicial y "Reintentar".
    fun load() {
        refreshState.value = RefreshState.Loading
        viewModelScope.launch {
            repository.refreshCatalog()
                .onSuccess { refreshState.value = RefreshState.Done }
                .onFailure { refreshState.value = RefreshState.Failed(it.toUserMessage()) }
        }
    }

    fun retry() = load()
}
