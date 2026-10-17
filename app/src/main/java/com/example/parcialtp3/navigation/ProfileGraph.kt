package com.example.parcialtp3.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.parcialtp3.ui.profile.EditProfileScreen
import com.example.parcialtp3.ui.profile.GoalsPreferencesScreen
import com.example.parcialtp3.ui.profile.NutritionPreferencesScreen
import com.example.parcialtp3.ui.profile.ProfileDashboardScreen
import com.example.parcialtp3.ui.profile.SettingsScreen
import com.example.parcialtp3.ui.profile.VoiceAiSettingsScreen

/**
 * Profile Flow. Guardar vuelve a la pantalla anterior. "Log out" limpia toda la pila y vuelve
 * al login (sin Firebase: perfil de demostracion).
 */
fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation(
        startDestination = AppDestination.ProfileDashboard.route,
        route = AppDestination.ProfileFlow.route,
    ) {
        composable(AppDestination.ProfileDashboard.route) {
            ProfileDashboardScreen(
                onSettingsClick = { navController.navigate(AppDestination.Settings.route) },
                onEditProfileClick = { navController.navigate(AppDestination.EditProfile.route) },
                onGoalsClick = { navController.navigate(AppDestination.GoalsPreferences.route) },
                onPreferencesClick = { navController.navigate(AppDestination.NutritionPreferences.route) },
            )
        }
        composable(AppDestination.EditProfile.route) {
            EditProfileScreen(
                onBackClick = { navController.popBackStack() },
                onSaveChangesClick = { navController.popBackStack() },
            )
        }
        composable(AppDestination.GoalsPreferences.route) {
            GoalsPreferencesScreen(
                onBackClick = { navController.popBackStack() },
                onSavePreferencesClick = { navController.popBackStack() },
            )
        }
        composable(AppDestination.NutritionPreferences.route) {
            NutritionPreferencesScreen(
                onBackClick = { navController.popBackStack() },
                onSavePreferencesClick = { navController.popBackStack() },
            )
        }
        composable(AppDestination.VoiceAiSettings.route) {
            VoiceAiSettingsScreen(
                onBackClick = { navController.popBackStack() },
                onPrivacyAndPermissionsClick = { navController.popBackStack() },
            )
        }
        composable(AppDestination.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onVoiceAiClick = { navController.navigate(AppDestination.VoiceAiSettings.route) },
                onLogOutClick = { navController.navigateToLoginClearingBackStack() },
            )
        }
    }
}
