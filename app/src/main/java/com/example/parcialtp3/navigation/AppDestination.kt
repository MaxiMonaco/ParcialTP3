package com.example.parcialtp3.navigation

sealed class AppDestination(val route: String) {
    // Autenticacion
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object ForgotPassword : AppDestination("forgot_password")

    // App (con sesion)
    data object Quote : AppDestination("quote")
    data object Favorites : AppDestination("favorites")
}
