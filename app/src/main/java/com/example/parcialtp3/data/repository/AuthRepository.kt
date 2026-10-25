package com.example.parcialtp3.data.repository

/**
 * Contrato de autenticacion. La UI y los ViewModels solo conocen esta interfaz:
 * no saben si atras hay Firebase o el acceso de demostracion.
 * Hilt decide que implementacion entregar (ver di/FirebaseModule.kt).
 */
interface AuthRepository {

    /** true = no hay Firebase configurado y se usa el acceso de demostracion. */
    val isDemo: Boolean

    /** Id del usuario con sesion abierta, o null si no hay sesion. */
    val currentUserId: String?

    /** Email del usuario con sesion abierta, o null si no hay sesion. */
    val currentUserEmail: String?

    // Si algo falla, estas funciones lanzan AuthException con un mensaje en español.
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun sendPasswordReset(email: String)

    fun signOut()
}

/** Error de autenticacion con un mensaje en español, listo para mostrar en pantalla. */
class AuthException(message: String, cause: Throwable? = null) : Exception(message, cause)