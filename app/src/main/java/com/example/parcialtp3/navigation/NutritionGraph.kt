package com.example.parcialtp3.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.parcialtp3.ui.nutrition.AddFoodScreen
import com.example.parcialtp3.ui.nutrition.FoodAnalysisScreen
import com.example.parcialtp3.ui.nutrition.FoodConfirmationScreen
import com.example.parcialtp3.ui.nutrition.ManualFoodScreen
import com.example.parcialtp3.ui.nutrition.MealDetailsScreen
import com.example.parcialtp3.ui.nutrition.NutritionDashboardScreen
import com.example.parcialtp3.ui.nutrition.PhotoAnalysisScreen
import com.example.parcialtp3.ui.nutrition.TodaysMealsScreen
import com.example.parcialtp3.ui.nutrition.VoiceFoodLoggingScreen

/**
 * Nutrition Flow. Dos caminos para agregar una comida: voz (04 -> 05 -> 07) y foto (06 -> 07),
 * ademas de la carga manual (03b, extra de RF03). Al confirmar se vuelve al Dashboard
 * descartando el camino de carga. El detalle de comida recibe el id por la ruta.
 */
fun NavGraphBuilder.nutritionGraph(navController: NavController) {
    navigation(
        startDestination = AppDestination.NutritionDashboard.route,
        route = AppDestination.NutritionFlow.route,
    ) {
        composable(AppDestination.NutritionDashboard.route) {
            NutritionDashboardScreen(
                onTodaysMealsClick = { navController.navigate(AppDestination.TodaysMeals.route) },
                onMealClick = { id -> navController.navigate(AppDestination.MealDetails.createRoute(id)) },
                onAddDinnerClick = { navController.navigate(AppDestination.AddFood.route) },
                onVoiceClick = { navController.navigate(AppDestination.VoiceFoodLogging.route) },
                onPhotoClick = { navController.navigate(AppDestination.PhotoAnalysis.route) },
                onSearchClick = { navController.navigate(AppDestination.AddFood.route) },
            )
        }
        composable(AppDestination.TodaysMeals.route) {
            TodaysMealsScreen(
                onMealClick = { id -> navController.navigate(AppDestination.MealDetails.createRoute(id)) },
                onAddDinnerClick = { navController.navigate(AppDestination.AddFood.route) },
            )
        }
        composable(AppDestination.AddFood.route) {
            AddFoodScreen(
                onBackClick = { navController.popBackStack() },
                onStartVoiceLoggingClick = { navController.navigate(AppDestination.VoiceFoodLogging.route) },
                onSnapPhotoClick = { navController.navigate(AppDestination.PhotoAnalysis.route) },
                onLogManuallyClick = { navController.navigate(AppDestination.ManualFood.route) },
            )
        }
        composable(AppDestination.ManualFood.route) {
            ManualFoodScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.navigateToTabRoot(AppDestination.NutritionDashboard.route) },
            )
        }
        composable(AppDestination.VoiceFoodLogging.route) {
            VoiceFoodLoggingScreen(
                onCloseClick = { navController.popBackStack() },
                onDoneSpeakingClick = { navController.navigate(AppDestination.FoodAnalysis.route) },
                onTypeInsteadClick = { navController.navigate(AppDestination.ManualFood.route) },
            )
        }
        composable(AppDestination.FoodAnalysis.route) {
            FoodAnalysisScreen(
                onCloseClick = { navController.popBackStack() },
                onAnalysisFinishedClick = { navController.navigate(AppDestination.FoodConfirmation.route) },
            )
        }
        composable(AppDestination.PhotoAnalysis.route) {
            PhotoAnalysisScreen(
                onBackClick = { navController.popBackStack() },
                onReviewAndAddClick = { navController.navigate(AppDestination.FoodConfirmation.route) },
                onRetakePhotoClick = { navController.popBackStack() },
            )
        }
        composable(AppDestination.FoodConfirmation.route) {
            FoodConfirmationScreen(
                onBackClick = { navController.popBackStack() },
                onAddToMealClick = { navController.navigateToTabRoot(AppDestination.NutritionDashboard.route) },
                onSaveWithoutLoggingClick = { navController.navigateToTabRoot(AppDestination.NutritionDashboard.route) },
            )
        }
        composable(
            route = AppDestination.MealDetails.route,
            arguments = listOf(navArgument(NavArgs.MEAL_ID) { type = NavType.StringType }),
        ) { entry ->
            MealDetailsScreen(
                mealId = entry.arguments?.getString(NavArgs.MEAL_ID).orEmpty(),
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
