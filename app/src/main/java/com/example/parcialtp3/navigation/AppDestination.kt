package com.example.parcialtp3.navigation

/** Nombres de los argumentos que viajan en la ruta (los que van entre llaves). */
object NavArgs {
    const val WORKOUT_ID = "workoutId"
    const val EXERCISE_ID = "exerciseId"
    const val MEAL_ID = "mealId"
}

/**
 * Todas las rutas de la app, una por pantalla de Figma (37) + 1 extra marcada abajo.
 * El nombre de cada ruta sigue el de Figma: "Workout / 05 — Active Workout" -> workout/05_active.
 *
 * Las rutas con argumento (detalle de entrenamiento, ejercicio, comida) tienen createRoute():
 * asi cada elemento abre SU propio detalle (NAV04) y no siempre el mismo.
 */
sealed class AppDestination(val route: String) {

    // ---------- Grafos: uno por flujo de Figma (no son pantallas) ----------
    data object LoginFlow : AppDestination("flow_login")
    data object HomeFlow : AppDestination("flow_home")
    data object VoiceFlow : AppDestination("flow_voice")
    data object WorkoutFlow : AppDestination("flow_workout")
    data object NutritionFlow : AppDestination("flow_nutrition")
    data object ProgressFlow : AppDestination("flow_progress")
    data object ProfileFlow : AppDestination("flow_profile")

    // ---------- Login Flow (2) ----------
    data object SignIn : AppDestination("login/01_signin")
    data object SignUp : AppDestination("login/02_signup")

    // ---------- Home Flow (2) ----------
    data object HomeDashboard : AppDestination("home/01_dashboard")
    data object Notifications : AppDestination("home/02_notifications")

    // ---------- AI Voice Flow (6) ----------
    data object VoiceIdle : AppDestination("voice/01_idle")
    data object VoiceListening : AppDestination("voice/02_listening")
    data object VoiceProcessing : AppDestination("voice/03_processing")
    data object VoiceSpeaking : AppDestination("voice/04_speaking")
    data object VoiceConversation : AppDestination("voice/05_conversation")
    data object VoiceError : AppDestination("voice/06_error")

    // ---------- Workout Flow (7) ----------
    data object WorkoutDashboard : AppDestination("workout/01_dashboard")
    data object WeeklyPlan : AppDestination("workout/02_weekly_plan")
    data object WorkoutDetails : AppDestination("workout/03_details/{${NavArgs.WORKOUT_ID}}") {
        fun createRoute(workoutId: String) = "workout/03_details/$workoutId"
    }
    data object PreWorkout : AppDestination("workout/04_pre/{${NavArgs.WORKOUT_ID}}") {
        fun createRoute(workoutId: String) = "workout/04_pre/$workoutId"
    }
    data object ActiveWorkout : AppDestination("workout/05_active/{${NavArgs.WORKOUT_ID}}") {
        fun createRoute(workoutId: String) = "workout/05_active/$workoutId"
    }
    data object ExerciseDetails : AppDestination("workout/06_exercise/{${NavArgs.EXERCISE_ID}}") {
        fun createRoute(exerciseId: String) = "workout/06_exercise/$exerciseId"
    }
    data object WorkoutComplete : AppDestination("workout/07_complete")

    // ---------- Nutrition Flow (8 + 1 extra) ----------
    data object NutritionDashboard : AppDestination("nutrition/01_dashboard")
    data object TodaysMeals : AppDestination("nutrition/02_todays_meals")
    data object AddFood : AppDestination("nutrition/03_add_food")
    data object VoiceFoodLogging : AppDestination("nutrition/04_voice_logging")
    data object FoodAnalysis : AppDestination("nutrition/05_food_analysis")
    data object PhotoAnalysis : AppDestination("nutrition/06_photo_analysis")
    data object FoodConfirmation : AppDestination("nutrition/07_food_confirmation")
    data object MealDetails : AppDestination("nutrition/08_meal/{${NavArgs.MEAL_ID}}") {
        fun createRoute(mealId: String) = "nutrition/08_meal/$mealId"
    }

    /** EXTRA: no esta en los 37 frames de Figma, pero "Log Manually" la necesita (RF03). */
    data object ManualFood : AppDestination("nutrition/03b_manual_food")

    // ---------- Progress Flow (6) ----------
    data object ProgressOverview : AppDestination("progress/01_todays_overview")
    data object ProgressDashboard : AppDestination("progress/02_dashboard")
    data object WorkoutProgress : AppDestination("progress/03_workout_progress")
    data object NutritionProgress : AppDestination("progress/04_nutrition_progress")
    data object HealthTrends : AppDestination("progress/05_health_trends")
    data object WeeklyReview : AppDestination("progress/06_weekly_review")

    // ---------- Profile Flow (6) ----------
    data object ProfileDashboard : AppDestination("profile/01_dashboard")
    data object EditProfile : AppDestination("profile/02_edit_profile")
    data object GoalsPreferences : AppDestination("profile/03_goals_preferences")
    data object NutritionPreferences : AppDestination("profile/04_nutrition_preferences")
    data object VoiceAiSettings : AppDestination("profile/05_voice_ai_settings")
    data object Settings : AppDestination("profile/06_settings")

    companion object {
        /**
         * Pantallas donde Figma muestra la barra inferior. En el resto (flujo de voz,
         * entrenamiento activo, detalles, etc.) la barra se oculta.
         */
        val withBottomBar: Set<String> by lazy {
            setOf(
            HomeDashboard.route, Notifications.route,
            WorkoutDashboard.route, WeeklyPlan.route,
            NutritionDashboard.route, TodaysMeals.route, AddFood.route,
            PhotoAnalysis.route, FoodConfirmation.route, MealDetails.route,
            ProgressOverview.route, ProgressDashboard.route, WorkoutProgress.route,
            NutritionProgress.route, HealthTrends.route,
            ProfileDashboard.route,
            )
        }
    }
}
