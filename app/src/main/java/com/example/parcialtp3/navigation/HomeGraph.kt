package com.example.parcialtp3.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.parcialtp3.ui.home.HomeDashboardScreen
import com.example.parcialtp3.ui.home.NotificationsScreen

/** Home Flow: Dashboard y Notifications. Desde el Dashboard se salta a voz, entrenamiento y comidas. */
fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(
        startDestination = AppDestination.HomeDashboard.route,
        route = AppDestination.HomeFlow.route,
    ) {
        composable(AppDestination.HomeDashboard.route) {
            HomeDashboardScreen(
                onNotificationsClick = { navController.navigate(AppDestination.Notifications.route) },
                onStartVoiceClick = { navController.navigate(AppDestination.VoiceIdle.route) },
                onStartWorkoutClick = {
                    navController.navigate(AppDestination.PreWorkout.createRoute(SampleIds.WORKOUT_UPPER))
                },
                onLogBreakfastClick = { navController.navigate(AppDestination.AddFood.route) },
            )
        }
        composable(AppDestination.Notifications.route) {
            NotificationsScreen(
                onBackClick = { navController.popBackStack() },
                onWeeklyReviewClick = { navController.navigate(AppDestination.WeeklyReview.route) },
                onWorkoutPlanClick = {
                    navController.navigate(AppDestination.WorkoutDetails.createRoute(SampleIds.WORKOUT_UPPER))
                },
                onNutritionGoalClick = { navController.navigateToTabRoot(AppDestination.NutritionDashboard.route) },
            )
        }
    }
}
