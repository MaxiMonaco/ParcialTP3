package com.example.parcialtp3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.data.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    // Room emite la lista cada vez que la tabla cambia, asi que despues de "Eliminar"
    // la pantalla se actualiza sola: no hay que volver a pedir nada.
    val uiState: StateFlow<FavoritesUiState> = favoritesRepository.favorites
        .map { quotes ->
            if (quotes.isEmpty()) FavoritesUiState.Empty else FavoritesUiState.Content(quotes)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState.Loading,
        )

    fun removeFavorite(id: String) {
        viewModelScope.launch { favoritesRepository.remove(id) }
    }
}
