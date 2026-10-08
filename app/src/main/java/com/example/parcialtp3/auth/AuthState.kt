package com.example.parcialtp3.auth

/**
 * Estado de autenticacion (sellado) que pide el enunciado, con sus 6 casos.
 * Lo va a exponer el ViewModel de auth y las pantallas de login, registro y
 * recuperacion lo usan para mostrar carga, errores o navegar.
 */
sealed interface AuthState {
    data object Idle : AuthState                                  // Inactivo
    data object Loading : AuthState                               // Cargando
    data object Authenticated : AuthState                         // Autenticado
    data object Unauthenticated : AuthState                       // No autenticado
    data class PasswordResetSent(val email: String) : AuthState   // Email de reseteo enviado
    data class Error(val message: String) : AuthState             // Error
}
