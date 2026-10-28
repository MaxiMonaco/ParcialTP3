package com.example.parcialtp3.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Raiz de la navegacion. Arma el Scaffold con la barra inferior (visible solo donde Figma la
 * muestra) y el NavHost con un grafo por flujo. La logica de cada flujo vive en su archivo
 * (LoginGraph.kt, WorkoutGraph.kt, ...), asi cada integrante toca el suyo sin pisarse.
 *
 * startDestination lo decide MainActivity: Login si no hay sesion, Home si ya habia una abierta.
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = AppDestination.LoginFlow.route,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = currentDestination?.route in AppDestination.withBottomBar

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                VoraBottomBar(
                    currentDestination = currentDestination,
                    onTabClick = { tab -> navController.navigateToTab(tab.flow) },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            loginGraph(navController)
            homeGraph(navController)
            voiceGraph(navController)
            workoutGraph(navController)
            nutritionGraph(navController)
            progressGraph(navController)
            profileGraph(navController)
        }
    }
}