package com.example.parcialtp3.viewmodel

import com.example.parcialtp3.model.Quote

/**
 * Estados de la pantalla Favoritos. "Empty" es un estado propio (y no una lista
 * vacia dentro de Content) porque la pantalla muestra algo distinto en ese caso.
 */
sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState // todavia no respondio Room
    data object Empty : FavoritesUiState   // no hay favoritos guardados
    data class Content(val quotes: List<Quote>) : FavoritesUiState
}
