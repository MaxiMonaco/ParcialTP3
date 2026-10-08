package com.example.parcialtp3.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.Normalizer

/**
 * Una fila de la tabla "favorite_quotes".
 * Cada propiedad del constructor es una columna.
 */
@Entity(tableName = "favorite_quotes")
data class FavoriteQuote(
    // Clave primaria: texto + autor normalizados (ver quoteId). Dos frases iguales
    // generan el mismo id, asi que no se puede guardar la misma frase dos veces.
    @PrimaryKey val id: String,
    val text: String,
    val author: String,
    val category: String,
)

/**
 * Arma el id de una frase: texto y autor en minusculas, sin tildes y sin espacios
 * de mas, unidos con "|". Asi "La Imaginacion " y "la imaginación" dan el mismo id.
 */
fun quoteId(text: String, author: String): String =
    "${normalize(text)}|${normalize(author)}"

private fun normalize(value: String): String =
    Normalizer.normalize(value, Normalizer.Form.NFD)
        .replace("\\p{M}+".toRegex(), "") // saca las tildes
        .lowercase()
        .replace("\\s+".toRegex(), " ")   // colapsa espacios multiples
        .trim()
