package com.example.parcialtp3.data.repository

import com.example.parcialtp3.model.Quote
import com.example.parcialtp3.model.toQuote
import com.example.parcialtp3.data.remote.QuotesApi
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

/**
 * Unico lugar que habla con la API de frases.
 * @Inject constructor: Hilt ya sabe construirla porque QuotesApi lo provee NetworkModule.
 */
class QuotesRepository @Inject constructor(
    private val api: QuotesApi,
) {

    // Result<Quote> es "una frase o un error". Asi el ViewModel no necesita try/catch.
    suspend fun getRandomQuote(): Result<Quote> =
        try {
            val dto = api.getQuotes().firstOrNull()
                ?: error("La API devolvio una lista vacia") // lista vacia -> error
            Result.success(dto.toQuote())
        } catch (e: CancellationException) {
            throw e // si se cancela la coroutine, hay que dejarla cancelar
        } catch (e: Exception) {
            Result.failure(e) // sin red, 4xx/5xx, JSON raro, etc.
        }
}
