package com.example.parcialtp3.auth

/**
 * Estado de autenticacion (sellado) con sus 6 casos. Lo expone AuthViewModel y lo usan
 * las pantallas de Sign In y Sign Up para mostrar carga, errores o navegar al Home.
 */
sealed interface AuthState {
    data object Idle : AuthState                                  // Inactivo
    data object Loading : AuthState                               // Cargando
    data object Authenticated : AuthState                         // Autenticado
    data object Unauthenticated : AuthState                       // No autenticado
    data class PasswordResetSent(val email: String) : AuthState   // Email de reseteo enviado
    data class Error(val message: String) : AuthState             // Error
}