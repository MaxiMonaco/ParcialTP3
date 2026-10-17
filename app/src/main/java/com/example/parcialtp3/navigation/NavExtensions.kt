package com.example.parcialtp3.navigation

import androidx.navigation.NavController

/**
 * Cambio de pestana de la barra inferior: guarda el estado de la pestana que deja y
 * restaura el de la que abre. Siempre se apila sobre Home, asi "atras" vuelve a Home.
 */
fun NavController.navigateToTab(tab: AppDestination) {
    navigate(tab.route) {
        popUpTo(AppDestination.HomeDashboard.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Va a la pantalla raiz de una pestana descartando lo que haya encima (por ejemplo, al terminar
 * un entrenamiento o confirmar una comida). Atras desde alli vuelve a Home.
 */
fun NavController.navigateToTabRoot(route: String) {
    navigate(route) {
        popUpTo(AppDestination.HomeDashboard.route)
        launchSingleTop = true
    }
}

/** Login correcto: Home pasa a ser la base y atras ya no vuelve al login. */
fun NavController.navigateToHomeClearingBackStack() {
    navigate(AppDestination.HomeFlow.route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

/** Cerrar sesion: limpia todo y vuelve al login. */
fun NavController.navigateToLoginClearingBackStack() {
    navigate(AppDestination.LoginFlow.route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

/** Vuelve a la pantalla Idle del asistente si esta en la pila; si no, la abre. */
fun NavController.returnToVoiceIdle() {
    if (!popBackStack(AppDestination.VoiceIdle.route, inclusive = false)) {
        navigate(AppDestination.VoiceIdle.route)
    }
}
