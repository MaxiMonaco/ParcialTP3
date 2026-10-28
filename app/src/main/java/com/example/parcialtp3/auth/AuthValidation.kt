package com.example.parcialtp3.auth

/**
 * Validaciones de los formularios de Sign In y Sign Up, antes de llamar a Firebase.
 * Son funciones puras (no dependen de Android): reciben texto y devuelven los errores.
 */

/** Un mensaje por campo; null = el campo esta bien. */
data class AuthFormErrors(
    val fullName: String? = null,
    val email: String? = null,
    val password: String? = null,
) {
    val hasErrors: Boolean
        get() = fullName != null || email != null || password != null
}

/** Figma pide "8+ secure characters". Firebase acepta desde 6, pero seguimos el diseño. */
const val MIN_PASSWORD_LENGTH = 8

// Algo@algo.algo, sin espacios. La validacion completa la hace Firebase.
private val EMAIL_SHAPE = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")

fun emailError(email: String): String? = when {
    email.isBlank() -> "Ingresá tu email."
    !EMAIL_SHAPE.matches(email.trim()) -> "El email no es válido."
    else -> null
}

/** Sign In: email con forma valida y contraseña no vacia (el largo lo controla Firebase). */
fun validateSignIn(email: String, password: String): AuthFormErrors = AuthFormErrors(
    email = emailError(email),
    password = if (password.isEmpty()) "Ingresá tu contraseña." else null,
)

/** Sign Up: nombre, email valido y contraseña de al menos 8 caracteres. */
fun validateSignUp(fullName: String, email: String, password: String): AuthFormErrors = AuthFormErrors(
    fullName = if (fullName.isBlank()) "Ingresá tu nombre." else null,
    email = emailError(email),
    password = when {
        password.isEmpty() -> "Ingresá una contraseña."
        password.length < MIN_PASSWORD_LENGTH -> "Usá al menos $MIN_PASSWORD_LENGTH caracteres."
        else -> null
    },
)