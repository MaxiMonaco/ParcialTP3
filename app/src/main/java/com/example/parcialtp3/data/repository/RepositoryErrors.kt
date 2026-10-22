package com.example.parcialtp3.data.repository

import retrofit2.HttpException
import java.io.IOException

/**
 * Traduce una excepcion de red a un mensaje para el usuario. Asi las pantallas no conocen
 * Retrofit ni OkHttp: solo reciben texto.
 *  - IOException: sin internet, servidor caido, timeout.
 *  - HttpException: el servidor respondio con 4xx/5xx (p. ej. 404 o el escenario "error" = 503).
 *  - Cualquier otra cosa (JSON invalido, escenario "malformed"): respuesta no interpretable.
 */
fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "No hay conexión con el servidor."
    is HttpException ->
        if (code() == 404) "El recurso no existe (404)." else "El servidor respondió con un error (${code()})."
    else -> "La respuesta del servidor no es válida."
}

/** true si el fallo fue de conectividad (no del contenido): es el caso "sin conexión". */
fun Throwable.isOffline(): Boolean = this is IOException
