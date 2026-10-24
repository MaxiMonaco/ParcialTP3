package com.example.parcialtp3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.data.repository.MealsRepository
import com.example.parcialtp3.model.Meal
import com.example.parcialtp3.model.MealDates
import com.example.parcialtp3.model.MealFormErrors
import com.example.parcialtp3.model.MealType
import com.example.parcialtp3.model.parseMealNumber
import com.example.parcialtp3.model.validateMealForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Todo lo que muestra el formulario de carga manual. Los numeros van como texto hasta validarlos. */
data class MealFormState(
    val name: String = "",
    val date: String = MealDates.today(),
    val type: MealType = MealType.LUNCH,
    val calories: String = "",
    val protein: String = "",
    val carbs: String = "",
    val fat: String = "",
    val errors: MealFormErrors = MealFormErrors(),
    val isSaving: Boolean = false,
)

/** Carga manual de una comida (RF03): valida (nombre no vacio, valores >= 0) y guarda en Room. */
@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val repository: MealsRepository,
) : ViewModel() {

    private val _form = MutableStateFlow(MealFormState())
    val form: StateFlow<MealFormState> = _form.asStateFlow()

    // Al editar un campo se borra su error: el usuario ve que ya lo esta corrigiendo.
    fun onNameChange(value: String) = _form.update { it.copy(name = value, errors = it.errors.copy(name = null)) }
    fun onDateChange(value: String) = _form.update { it.copy(date = value, errors = it.errors.copy(date = null)) }
    fun onTypeChange(value: MealType) = _form.update { it.copy(type = value) }
    fun onCaloriesChange(value: String) = _form.update { it.copy(calories = value, errors = it.errors.copy(calories = null)) }
    fun onProteinChange(value: String) = _form.update { it.copy(protein = value, errors = it.errors.copy(protein = null)) }
    fun onCarbsChange(value: String) = _form.update { it.copy(carbs = value, errors = it.errors.copy(carbs = null)) }
    fun onFatChange(value: String) = _form.update { it.copy(fat = value, errors = it.errors.copy(fat = null)) }

    /** Valida y, si todo esta bien, guarda y llama a onSaved (la pantalla navega al Dashboard). */
    fun save(onSaved: () -> Unit) {
        val current = _form.value
        if (current.isSaving) return // evita guardar dos veces con un doble toque
        val errors = validateMealForm(
            name = current.name,
            date = current.date,
            calories = current.calories,
            protein = current.protein,
            carbs = current.carbs,
            fat = current.fat,
        )
        if (errors.hasErrors) {
            _form.update { it.copy(errors = errors) }
            return
        }
        val meal = Meal(
            id = 0,
            name = current.name.trim(),
            type = current.type,
            date = current.date.trim(),
            calories = parseMealNumber(current.calories) ?: 0.0,
            protein = parseMealNumber(current.protein) ?: 0.0,
            carbs = parseMealNumber(current.carbs) ?: 0.0,
            fat = parseMealNumber(current.fat) ?: 0.0,
        )
        persist(meal, onSaved)
    }

    /**
     * Pantalla 07 (Food Confirmation): el analisis por voz/foto es simulado, pero lo que "confirma"
     * se guarda de verdad en Room como una comida mas del dia.
     */
    fun saveAnalysisDemo(onSaved: () -> Unit) {
        persist(
            Meal(
                id = 0,
                name = "Grilled chicken, rice and vegetables",
                type = MealType.LUNCH,
                date = MealDates.today(),
                calories = 520.0,
                protein = 38.0,
                carbs = 54.0,
                fat = 14.0,
            ),
            onSaved,
        )
    }

    private fun persist(meal: Meal, onSaved: () -> Unit) {
        _form.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            repository.add(meal)
            _form.update { it.copy(isSaving = false) }
            onSaved()
        }
    }
}
