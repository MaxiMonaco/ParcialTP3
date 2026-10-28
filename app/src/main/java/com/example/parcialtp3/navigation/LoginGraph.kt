package com.example.parcialtp3.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.parcialtp3.auth.AuthState
import com.example.parcialtp3.ui.login.SignInScreen
import com.example.parcialtp3.ui.login.SignUpScreen
import com.example.parcialtp3.viewmodel.AuthViewModel

/**
 * Login Flow: SignIn <-> SignUp. Cada pantalla tiene su AuthViewModel (lo crea Hilt) y recibe
 * solo datos y lambdas. Cuando el estado pasa a Authenticated (login, registro o sesion ya
 * abierta), se va al Home limpiando la pila: atras ya no vuelve al login.
 */
fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(
        startDestination = AppDestination.SignIn.route,
        route = AppDestination.LoginFlow.route,
    ) {
        composable(AppDestination.SignIn.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val form by viewModel.form.collectAsStateWithLifecycle()
            val authState by viewModel.authState.collectAsStateWithLifecycle()

            LaunchedEffect(authState) {
                if (authState == AuthState.Authenticated) navController.navigateToHomeClearingBackStack()
            }

            SignInScreen(
                form = form,
                authState = authState,
                isDemo = viewModel.isDemo,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onSignInClick = viewModel::signIn,
                onForgotPasswordClick = viewModel::sendPasswordReset,
                onSignUpClick = { navController.navigate(AppDestination.SignUp.route) },
                onMessageShown = viewModel::messageShown,
            )
        }
        composable(AppDestination.SignUp.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            val form by viewModel.form.collectAsStateWithLifecycle()
            val authState by viewModel.authState.collectAsStateWithLifecycle()

            LaunchedEffect(authState) {
                if (authState == AuthState.Authenticated) navController.navigateToHomeClearingBackStack()
            }

            SignUpScreen(
                form = form,
                authState = authState,
                isDemo = viewModel.isDemo,
                onFullNameChange = viewModel::onFullNameChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onSignUpClick = viewModel::signUp,
                onRecoverClick = viewModel::sendPasswordReset,
                onSignInClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
                onMessageShown = viewModel::messageShown,
            )
        }
    }
}