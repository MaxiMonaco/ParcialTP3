package com.example.parcialtp3.navigation

/**
 * Ids de ejemplo para los accesos directos (Home, Progress) que abren un entrenamiento sin pasar
 * por el catalogo. Coinciden con ids reales de la API (w01, w02, w03); si el servidor cambia, se rompen.
 *
 * Notas anteriores:
 * Cuando Workout use Retrofit, el id sale del catalogo (GET /v1/workouts); cuando Nutrition
 * use Room, el id de la comida sale de la base. Estos valores solo prueban NAV04.
 */
object SampleIds {
    const val WORKOUT_UPPER = "w01"
    const val WORKOUT_LOWER = "w02"

    const val EXERCISE_BENCH = "bench-press"
    const val EXERCISE_CABLE_ROW = "seated-cable-row"

    const val MEAL_BREAKFAST = "breakfast"
    const val MEAL_LUNCH = "lunch"
    const val MEAL_SNACK = "snack"
}
