package com.example.parcialtp3.model

import com.example.parcialtp3.data.local.MealEntity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Tipo de comida (RF03). `label` es el texto que muestra la UI (igual que en Figma). */
enum class MealType(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    SNACK("Snack"),
    DINNER("Dinner"),
}

data class Meal(
    val id: Long,
    val name: String,
    val type: MealType,
    val date: String, // yyyy-MM-dd
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
)

/** Suma de un dia: es el "total diario" que cambia al agregar o eliminar comidas (RF04). */
data class DayTotals(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
)

fun List<Meal>.totals(): DayTotals = DayTotals(
    calories = sumOf { it.calories },
    protein = sumOf { it.protein },
    carbs = sumOf { it.carbs },
    fat = sumOf { it.fat },
)

// ---------- Room <-> app ----------

fun MealEntity.toMeal() = Meal(
    id = id,
    name = name,
    type = MealType.entries.firstOrNull { it.name == type } ?: MealType.SNACK,
    date = date,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
)

fun Meal.toEntity(createdAt: Long = System.currentTimeMillis()) = MealEntity(
    id = id,
    name = name,
    type = type.name,
    date = date,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    createdAt = createdAt,
)

// ---------- Fechas ----------
// Se usa java.text/java.util y no java.time porque minSdk = 24 y java.time pide API 26+.

object MealDates {
    private const val PATTERN = "yyyy-MM-dd"
    private val SHAPE = Regex("\\d{4}-\\d{2}-\\d{2}")

    private fun formatter(): SimpleDateFormat = SimpleDateFormat(PATTERN, Locale.US).apply { isLenient = false }

    fun today(): String = formatter().format(Calendar.getInstance().time)

    /** true solo si el texto es una fecha real con forma AAAA-MM-DD (rechaza 2026-02-31). */
    fun isValid(text: String): Boolean {
        if (!SHAPE.matches(text)) return false
        return try {
            formatter().parse(text) != null
        } catch (e: ParseException) {
            false
        }
    }

    /** Mueve una fecha `days` dias (negativo = hacia atras). Si el texto no es valido, lo devuelve igual. */
    fun shift(date: String, days: Int): String {
        val parsed = try {
            formatter().parse(date)
        } catch (e: ParseException) {
            null
        } ?: return date
        val calendar = Calendar.getInstance()
        calendar.time = parsed
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return formatter().format(calendar.time)
    }

    /** "Wednesday, 21 October" para mostrar en pantalla. */
    fun pretty(date: String): String {
        val parsed = try {
            formatter().parse(date)
        } catch (e: ParseException) {
            null
        } ?: return date
        return SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH).format(parsed)
    }
}

// ---------- Validacion del formulario (RF03) ----------

/** Un mensaje por campo; null = el campo esta bien. */
data class MealFormErrors(
    val name: String? = null,
    val date: String? = null,
    val calories: String? = null,
    val protein: String? = null,
    val carbs: String? = null,
    val fat: String? = null,
) {
    val hasErrors: Boolean
        get() = listOf(name, date, calories, protein, carbs, fat).any { it != null }
}

/** Acepta coma o punto decimal. Devuelve null si no es un numero finito. */
fun parseMealNumber(text: String): Double? {
    val value = text.trim().replace(',', '.').toDoubleOrNull() ?: return null
    return if (value.isNaN() || value.isInfinite()) null else value
}

private fun numberError(text: String): String? {
    if (text.isBlank()) return "Ingresá un número."
    val value = parseMealNumber(text) ?: return "Ingresá un número válido."
    return if (value < 0.0) "No puede ser negativo." else null
}

/** Reglas de la consigna: nombre no vacio y valores no negativos (ademas, fecha real y numeros presentes). */
fun validateMealForm(
    name: String,
    date: String,
    calories: String,
    protein: String,
    carbs: String,
    fat: String,
): MealFormErrors = MealFormErrors(
    name = if (name.isBlank()) "Ingresá un nombre." else null,
    date = if (MealDates.isValid(date.trim())) null else "Fecha inválida (usá AAAA-MM-DD).",
    calories = numberError(calories),
    protein = numberError(protein),
    carbs = numberError(carbs),
    fat = numberError(fat),
)
