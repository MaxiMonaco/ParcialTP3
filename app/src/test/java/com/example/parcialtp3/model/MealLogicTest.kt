package com.example.parcialtp3.model

import com.example.parcialtp3.data.local.MealEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas de la logica pura de comidas (RF03 validacion, RF04 total diario): corren en la JVM,
 * sin emulador. Ejecutar con: ./gradlew :app:testDebugUnitTest
 */
class MealLogicTest {

    private fun errors(
        name: String = "Chicken",
        date: String = "2026-10-21",
        calories: String = "280",
        protein: String = "38",
        carbs: String = "54",
        fat: String = "14",
    ) = validateMealForm(name, date, calories, protein, carbs, fat)

    @Test
    fun formularioCompletoEsValido() {
        assertFalse(errors().hasErrors)
    }

    @Test
    fun nombreVacioOEnBlancoEsInvalido() {
        assertEquals("Ingresá un nombre.", errors(name = "").name)
        assertEquals("Ingresá un nombre.", errors(name = "   ").name)
    }

    @Test
    fun valoresNegativosSonInvalidos() {
        assertEquals("No puede ser negativo.", errors(calories = "-1").calories)
        assertEquals("No puede ser negativo.", errors(protein = "-0,5").protein)
        assertEquals("No puede ser negativo.", errors(carbs = "-10").carbs)
        assertEquals("No puede ser negativo.", errors(fat = "-3").fat)
    }

    @Test
    fun ceroEsValido() {
        assertFalse(errors(calories = "0", protein = "0", carbs = "0", fat = "0").hasErrors)
    }

    @Test
    fun camposNumericosVaciosOConTextoSonInvalidos() {
        assertEquals("Ingresá un número.", errors(calories = "").calories)
        assertEquals("Ingresá un número válido.", errors(fat = "abc").fat)
        assertTrue(errors(calories = "NaN").hasErrors)
        assertTrue(errors(calories = "Infinity").hasErrors)
    }

    @Test
    fun aceptaComaOPuntoDecimal() {
        assertEquals(12.5, parseMealNumber("12,5")!!, 0.0001)
        assertEquals(12.5, parseMealNumber(" 12.5 ")!!, 0.0001)
        assertNull(parseMealNumber("12,5,1"))
    }

    @Test
    fun fechaDebeSerRealYTenerFormato() {
        assertTrue(MealDates.isValid("2026-10-21"))
        assertFalse(MealDates.isValid("2026-02-31"))
        assertFalse(MealDates.isValid("21/10/2026"))
        assertFalse(MealDates.isValid("2026-10-21 extra"))
        assertEquals("Fecha inválida (usá AAAA-MM-DD).", errors(date = "hoy").date)
    }

    @Test
    fun moverFechaCruzaMesesYAnios() {
        assertEquals("2026-11-01", MealDates.shift("2026-10-31", 1))
        assertEquals("2026-02-28", MealDates.shift("2026-03-01", -1))
        assertEquals("2027-01-01", MealDates.shift("2026-12-31", 1))
        assertEquals("fecha-rota", MealDates.shift("fecha-rota", 1))
    }

    @Test
    fun totalDiarioSumaLasComidasYCambiaAlQuitarUna() {
        val a = Meal(1, "A", MealType.BREAKFAST, "2026-10-21", 400.0, 20.0, 50.0, 10.0)
        val b = Meal(2, "B", MealType.LUNCH, "2026-10-21", 520.0, 38.0, 54.0, 14.0)
        val total = listOf(a, b).totals()
        assertEquals(920.0, total.calories, 0.0001)
        assertEquals(58.0, total.protein, 0.0001)
        assertEquals(104.0, total.carbs, 0.0001)
        assertEquals(24.0, total.fat, 0.0001)
        assertEquals(400.0, listOf(a).totals().calories, 0.0001)
        assertEquals(0.0, emptyList<Meal>().totals().calories, 0.0001)
    }

    @Test
    fun mapperRoomUsaSnackSiElTipoNoSeReconoce() {
        val entity = MealEntity(id = 7, name = "X", type = "ALGO_RARO", date = "2026-10-21",
            calories = 1.0, protein = 2.0, carbs = 3.0, fat = 4.0, createdAt = 0L)
        assertEquals(MealType.SNACK, entity.toMeal().type)
        val round = Meal(7, "X", MealType.DINNER, "2026-10-21", 1.0, 2.0, 3.0, 4.0).toEntity(createdAt = 5L)
        assertEquals("DINNER", round.type)
        assertEquals(MealType.DINNER, round.toMeal().type)
    }
}
