package com.example.parcialtp3.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.parcialtp3.ui.workout.ActiveWorkoutScreen
import com.example.parcialtp3.ui.workout.ExerciseDetailsScreen
import com.example.parcialtp3.ui.workout.PreWorkoutScreen
import com.example.parcialtp3.ui.workout.WeeklyPlanScreen
import com.example.parcialtp3.ui.workout.WorkoutCompleteScreen
import com.example.parcialtp3.ui.workout.WorkoutDashboardScreen
import com.example.parcialtp3.ui.workout.WorkoutDetailsScreen

/**
 * Workout Flow. Detalle, preparacion, sesion activa y ejercicio reciben el id por la ruta:
 * cada entrenamiento abre SU detalle (NAV04). Al terminar se vuelve al Dashboard descartando
 * la sesion (Pre -> Active -> Complete), para que atras no reabra el entrenamiento.
 */
fun NavGraphBuilder.workoutGraph(navController: NavController) {
    navigation(
        startDestination = AppDestination.WorkoutDashboard.route,
        route = AppDestination.WorkoutFlow.route,
    ) {
        composable(AppDestination.WorkoutDashboard.route) {
            WorkoutDashboardScreen(
                onStartWorkoutClick = { id -> navController.navigate(AppDestination.PreWorkout.createRoute(id)) },
                onWorkoutDetailsClick = { id -> navController.navigate(AppDestination.WorkoutDetails.createRoute(id)) },
                onWeeklyPlanClick = { navController.navigate(AppDestination.WeeklyPlan.route) },
                onViewProgressClick = { navController.navigate(AppDestination.WorkoutProgress.route) },
            )
        }
        composable(AppDestination.WeeklyPlan.route) {
            WeeklyPlanScreen(
                onWorkoutClick = { id -> navController.navigate(AppDestination.WorkoutDetails.createRoute(id)) },
                onAskVoraClick = { navController.navigate(AppDestination.VoiceIdle.route) },
            )
        }
        composable(
            route = AppDestination.WorkoutDetails.route,
            arguments = listOf(navArgument(NavArgs.WORKOUT_ID) { type = NavType.StringType }),
        ) { entry ->
            val workoutId = entry.arguments?.getString(NavArgs.WORKOUT_ID).orEmpty()
            WorkoutDetailsScreen(
                workoutId = workoutId,
                onBackClick = { navController.popBackStack() },
                onStartWorkoutClick = { navController.navigate(AppDestination.PreWorkout.createRoute(workoutId)) },
                onExerciseClick = { id -> navController.navigate(AppDestination.ExerciseDetails.createRoute(id)) },
            )
        }
        composable(
            route = AppDestination.PreWorkout.route,
            arguments = listOf(navArgument(NavArgs.WORKOUT_ID) { type = NavType.StringType }),
        ) { entry ->
            val workoutId = entry.arguments?.getString(NavArgs.WORKOUT_ID).orEmpty()
            PreWorkoutScreen(
                workoutId = workoutId,
                onCloseClick = { navController.popBackStack() },
                onStartWorkoutClick = { navController.navigate(AppDestination.ActiveWorkout.createRoute(workoutId)) },
                onAskVoraClick = { navController.navigate(AppDestination.VoiceIdle.route) },
            )
        }
        composable(
            route = AppDestination.ActiveWorkout.route,
            arguments = listOf(navArgument(NavArgs.WORKOUT_ID) { type = NavType.StringType }),
        ) { entry ->
            val workoutId = entry.arguments?.getString(NavArgs.WORKOUT_ID).orEmpty()
            ActiveWorkoutScreen(
                workoutId = workoutId,
                onFinishWorkoutClick = { navController.navigate(AppDestination.WorkoutComplete.route) },
                onExerciseGuideClick = { id -> navController.navigate(AppDestination.ExerciseDetails.createRoute(id)) },
            )
        }
        composable(
            route = AppDestination.ExerciseDetails.route,
            arguments = listOf(navArgument(NavArgs.EXERCISE_ID) { type = NavType.StringType }),
        ) { entry ->
            ExerciseDetailsScreen(
                exerciseId = entry.arguments?.getString(NavArgs.EXERCISE_ID).orEmpty(),
                onBackClick = { navController.popBackStack() },
                onBackToWorkoutClick = { navController.popBackStack() },
            )
        }
        composable(AppDestination.WorkoutComplete.route) {
            WorkoutCompleteScreen(
                onDoneClick = { navController.navigateToTabRoot(AppDestination.WorkoutDashboard.route) },
            )
        }
    }
}
