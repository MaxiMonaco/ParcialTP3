package com.example.parcialtp3.data.remote

/**
 * Un elemento del JSON que devuelve la API:
 * [ { "quote": "...", "author": "...", "category": "..." } ]
 *
 * Los nombres de las propiedades tienen que coincidir con las claves del JSON,
 * porque Gson las mapea por nombre. Si no coincidieran, hay que usar @SerializedName.
 */
data class QuoteDto(
    val quote: String,
    val author: String,
    val category: String,
)
