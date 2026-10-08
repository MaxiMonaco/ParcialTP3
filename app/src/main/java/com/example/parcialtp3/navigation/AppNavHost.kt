package com.example.parcialtp3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parcialtp3.ui.auth.ForgotPasswordScreen
import com.example.parcialtp3.ui.auth.LoginScreen
import com.example.parcialtp3.ui.auth.RegisterScreen
import com.example.parcialtp3.ui.favorites.FavoritesScreen
import com.example.parcialtp3.ui.quote.QuoteScreen
import com.example.parcialtp3.viewmodel.FavoritesViewModel
import com.example.parcialtp3.viewmodel.QuoteViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    // TODO: startDestination = Quote si hay sesion guardada (Firebase currentUser)
    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route,
        modifier = modifier,
    ) {
        composable(AppDestination.Login.route) {
            LoginScreen(
                // Entra: saca las pantallas de autenticacion del back stack
                onLoginSuccess = {
                    navController.navigate(AppDestination.Quote.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = { navController.navigate(AppDestination.Register.route) },
                onForgotPasswordClick = { navController.navigate(AppDestination.ForgotPassword.route) },
            )
        }
        composable(AppDestination.Register.route) {
            RegisterScreen(onBackToLoginClick = { navController.popBackStack() })
        }
        composable(AppDestination.ForgotPassword.route) {
            ForgotPasswordScreen(onBackToLoginClick = { navController.popBackStack() })
        }
        composable(AppDestination.Quote.route) {
            // hiltViewModel() pide a Hilt el ViewModel, atado a esta entrada del back stack.
            val viewModel: QuoteViewModel = hiltViewModel()
            // collectAsStateWithLifecycle: deja de escuchar si la app pasa a segundo plano.
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            QuoteScreen(
                uiState = uiState,
                onToggleFavorite = viewModel::toggleFavorite,
                onNextQuote = viewModel::loadQuote,
                onRetry = viewModel::loadQuote,
                onFavoritesClick = { navController.navigate(AppDestination.Favorites.route) },
                // Salir: limpia el stack y vuelve a login; atras no regresa a la frase
                onLogoutClick = {
                    navController.navigate(AppDestination.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(AppDestination.Favorites.route) {
            val viewModel: FavoritesViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            FavoritesScreen(
                uiState = uiState,
                // Volver regresa a la frase
                onBackClick = { navController.popBackStack() },
                onRemoveClick = viewModel::removeFavorite,
            )
        }
    }
}
