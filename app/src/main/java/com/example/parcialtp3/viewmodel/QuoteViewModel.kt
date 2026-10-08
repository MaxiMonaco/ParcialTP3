package com.example.parcialtp3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.data.repository.FavoritesRepository
import com.example.parcialtp3.data.repository.QuotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Hilt sabe crear este ViewModel y le pasa lo que pide el constructor
class QuoteViewModel @Inject constructor(
    private val quotesRepository: QuotesRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    // Estado "de la frase": cargando, error o contenido. Solo el ViewModel lo modifica.
    private val quoteState = MutableStateFlow<QuoteUiState>(QuoteUiState.Loading)

    // Lo que ve la pantalla. Cuando hay una frase, le agrega si es favorita o no
    // escuchando a Room: si se guarda o se quita, el estado cambia solo.
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<QuoteUiState> = quoteState
        .flatMapLatest { state ->
            if (state is QuoteUiState.Content) {
                favoritesRepository.isFavorite(state.quote.id)
                    .map { isFavorite -> state.copy(isFavorite = isFavorite) }
            } else {
                flowOf(state)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QuoteUiState.Loading,
        )

    init {
        loadQuote() // "Al entrar carga una frase"
    }

    // Lo llaman "Otra frase" y "Reintentar".
    fun loadQuote() {
        quoteState.value = QuoteUiState.Loading
        viewModelScope.launch {
            quotesRepository.getRandomQuote()
                .onSuccess { quote -> quoteState.value = QuoteUiState.Content(quote) }
                .onFailure {
                    quoteState.value =
                        QuoteUiState.Error("No pudimos cargar la frase. Revisá tu conexión.")
                }
        }
    }

    // "Guardar / Quitar favorito": segun el estado actual, hace una cosa o la otra.
    fun toggleFavorite() {
        val current = uiState.value as? QuoteUiState.Content ?: return
        viewModelScope.launch {
            if (current.isFavorite) {
                favoritesRepository.remove(current.quote.id)
            } else {
                favoritesRepository.add(current.quote)
            }
        }
    }
}
