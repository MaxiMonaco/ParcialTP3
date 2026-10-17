package com.example.parcialtp3.navigation

/**
 * Ids de ejemplo para recorrer la navegacion mientras no hay datos reales.
 * Cuando Workout use Retrofit, el id sale del catalogo (GET /v1/workouts); cuando Nutrition
 * use Room, el id de la comida sale de la base. Estos valores solo prueban NAV04.
 */
object SampleIds {
    const val WORKOUT_UPPER = "upper-body-strength"
    const val WORKOUT_LOWER = "lower-body-strength"

    const val EXERCISE_BENCH = "bench-press"
    const val EXERCISE_CABLE_ROW = "seated-cable-row"

    const val MEAL_BREAKFAST = "breakfast"
    const val MEAL_LUNCH = "lunch"
    const val MEAL_SNACK = "snack"
}
