package com.example.parcialtp3.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.parcialtp3.ui.progress.HealthTrendsScreen
import com.example.parcialtp3.ui.progress.NutritionProgressScreen
import com.example.parcialtp3.ui.progress.ProgressDashboardScreen
import com.example.parcialtp3.ui.progress.ProgressOverviewScreen
import com.example.parcialtp3.ui.progress.WeeklyReviewScreen
import com.example.parcialtp3.ui.progress.WorkoutProgressScreen

/**
 * Progress Flow. La pestana abre "Today's Overview" (pantalla de referencia de la consigna);
 * el Dashboard cuelga de ahi y reparte a las vistas de entrenamiento, nutricion, actividad y
 * revision semanal. Todo con datos de demostracion.
 */
fun NavGraphBuilder.progressGraph(navController: NavController) {
    navigation(
        startDestination = AppDestination.ProgressOverview.route,
        route = AppDestination.ProgressFlow.route,
    ) {
        composable(AppDestination.ProgressOverview.route) {
            ProgressOverviewScreen(
                onStartWorkoutClick = {
                    navController.navigate(AppDestination.PreWorkout.createRoute(SampleIds.WORKOUT_UPPER))
                },
                onOpenDashboardClick = { navController.navigate(AppDestination.ProgressDashboard.route) },
            )
        }
        composable(AppDestination.ProgressDashboard.route) {
            ProgressDashboardScreen(
                onBackClick = { navController.popBackStack() },
                onWorkoutViewClick = { navController.navigate(AppDestination.WorkoutProgress.route) },
                onNutritionViewClick = { navController.navigate(AppDestination.NutritionProgress.route) },
                onActivityViewClick = { navController.navigate(AppDestination.HealthTrends.route) },
                onSeeWeeklyReviewClick = { navController.navigate(AppDestination.WeeklyReview.route) },
            )
        }
        composable(AppDestination.WorkoutProgress.route) {
            WorkoutProgressScreen(onBackClick = { navController.popBackStack() })
        }
        composable(AppDestination.NutritionProgress.route) {
            NutritionProgressScreen(onBackClick = { navController.popBackStack() })
        }
        composable(AppDestination.HealthTrends.route) {
            HealthTrendsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(AppDestination.WeeklyReview.route) {
            WeeklyReviewScreen(
                onBackClick = { navController.popBackStack() },
                onAskVoraClick = { navController.navigate(AppDestination.VoiceIdle.route) },
                onDoneClick = { navController.popBackStack() },
            )
        }
    }
}
