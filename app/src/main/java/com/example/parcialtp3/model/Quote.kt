package com.example.parcialtp3.model

import com.example.parcialtp3.data.local.FavoriteQuote
import com.example.parcialtp3.data.local.quoteId
import com.example.parcialtp3.data.remote.QuoteDto

/**
 * Modelo que usa la app (pantallas y ViewModels). Las capas de abajo tienen su
 * propio modelo (QuoteDto para la red, FavoriteQuote para Room), y estos
 * mappers los convierten. Asi un cambio en la API o en la tabla no rompe la UI.
 */
data class Quote(
    val id: String,
    val text: String,
    val author: String,
    val category: String,
)

// Red -> app. El id no viene de la API: lo armamos con texto + autor normalizados.
fun QuoteDto.toQuote() = Quote(
    id = quoteId(quote, author),
    text = quote,
    author = author,
    category = category,
)

// Room -> app
fun FavoriteQuote.toQuote() = Quote(id = id, text = text, author = author, category = category)

// app -> Room
fun Quote.toEntity() = FavoriteQuote(id = id, text = text, author = author, category = category)
