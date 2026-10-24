package com.example.parcialtp3.data.remote

/**
 * Envoltorio de las respuestas exitosas de la VORA Mock API: { "data": ... }.
 * `data` es nullable a proposito: Gson no respeta los tipos no nulos de Kotlin, asi que si el
 * servidor manda algo raro el repositorio lo detecta (data == null) en vez de explotar mas tarde.
 */
data class ApiResponse<T>(
    val data: T?,
)
