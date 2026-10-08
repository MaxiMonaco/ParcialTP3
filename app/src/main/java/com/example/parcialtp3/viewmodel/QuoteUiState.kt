package com.example.parcialtp3.viewmodel

import com.example.parcialtp3.model.Quote

/**
 * Los 3 estados de la pantalla Frase que pide el enunciado:
 * cargando, error y contenido. Una sealed interface obliga a la UI a
 * manejar todos los casos (el "when" no compila si falta uno).
 */
sealed interface QuoteUiState {
    data object Loading : QuoteUiState
    data class Error(val message: String) : QuoteUiState
    data class Content(val quote: Quote, val isFavorite: Boolean = false) : QuoteUiState
}
