package com.example.parcialtp3.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.parcialtp3.ui.login.SignInScreen
import com.example.parcialtp3.ui.login.SignUpScreen

/** Login Flow: SignIn <-> SignUp. Al entrar se limpia la pila: atras ya no vuelve al login. */
fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(
        startDestination = AppDestination.SignIn.route,
        route = AppDestination.LoginFlow.route,
    ) {
        composable(AppDestination.SignIn.route) {
            SignInScreen(
                onSignInClick = { navController.navigateToHomeClearingBackStack() },
                onSignUpClick = { navController.navigate(AppDestination.SignUp.route) },
            )
        }
        composable(AppDestination.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { navController.navigateToHomeClearingBackStack() },
                onSignInClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
