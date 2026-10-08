package com.example.parcialtp3.data.remote

import retrofit2.http.GET

interface QuotesApi {

    // GET https://api.api-ninjas.com/v1/quotes
    // La parte "https://api.api-ninjas.com/" es la baseUrl (ver NetworkModule);
    // aca va solo el resto del camino, sin "/" al principio.
    // suspend: se llama desde una coroutine, nunca desde el hilo principal.
    // Devuelve una LISTA aunque venga una sola frase.
    @GET("v1/quotes")
    suspend fun getQuotes(): List<QuoteDto>
}
