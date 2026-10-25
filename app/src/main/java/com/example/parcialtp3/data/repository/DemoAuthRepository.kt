package com.example.parcialtp3.data.repository

import javax.inject.Inject

/**
 * Acceso de demostracion: se usa cuando no hay google-services.json en app/.
 * Acepta cualquier email y contraseña y no guarda nada: es el mismo comportamiento
 * que tenia la app antes de agregar Firebase (la consigna lo permite).
 */
class DemoAuthRepository @Inject constructor() : AuthRepository {

    private var signedInEmail: String? = null

    override val isDemo = true

    override val currentUserId: String?
        get() = if (signedInEmail != null) DEMO_USER_ID else null

    override val currentUserEmail: String?
        get() = signedInEmail

    override suspend fun signIn(email: String, password: String) {
        signedInEmail = email.ifBlank { DEMO_EMAIL }
    }

    override suspend fun signUp(email: String, password: String) = signIn(email, password)

    override suspend fun sendPasswordReset(email: String) {
        throw AuthException("Recuperar la contraseña no está disponible en el modo de demostración.")
    }

    override fun signOut() {
        signedInEmail = null
    }

    private companion object {
        const val DEMO_USER_ID = "demo"
        const val DEMO_EMAIL = "demo@vora.app"
    }
}