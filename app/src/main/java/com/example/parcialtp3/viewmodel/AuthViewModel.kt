package com.example.parcialtp3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcialtp3.auth.AuthFormErrors
import com.example.parcialtp3.auth.AuthState
import com.example.parcialtp3.auth.emailError
import com.example.parcialtp3.auth.validateSignIn
import com.example.parcialtp3.auth.validateSignUp
import com.example.parcialtp3.data.repository.AuthException
import com.example.parcialtp3.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Lo que muestran los formularios de Sign In y Sign Up. */
data class AuthFormState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val errors: AuthFormErrors = AuthFormErrors(),
)

/**
 * Maneja el login, el registro, la recuperacion de contraseña y el cierre de sesion.
 * Las pantallas observan `form` (campos y errores) y `authState` (carga, error, sesion abierta)
 * y le avisan al ViewModel lo que hace el usuario. Nunca hablan con Firebase directamente.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository, // Firebase o demostracion: lo decide Hilt
) : ViewModel() {

    private val _form = MutableStateFlow(AuthFormState())
    val form: StateFlow<AuthFormState> = _form.asStateFlow()

    // Si ya habia una sesion abierta (Firebase la recuerda), arranca como Authenticated.
    private val _authState = MutableStateFlow(
        if (repository.currentUserId != null) AuthState.Authenticated else AuthState.Unauthenticated
    )
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /** true = sin Firebase: las pantallas pueden avisar que es una demostracion. */
    val isDemo: Boolean
        get() = repository.isDemo

    // Al editar un campo se borra su error: el usuario ve que lo esta corrigiendo.
    fun onFullNameChange(value: String) =
        _form.update { it.copy(fullName = value, errors = it.errors.copy(fullName = null)) }

    fun onEmailChange(value: String) =
        _form.update { it.copy(email = value, errors = it.errors.copy(email = null)) }

    fun onPasswordChange(value: String) =
        _form.update { it.copy(password = value, errors = it.errors.copy(password = null)) }

    fun signIn() {
        val current = _form.value
        // En modo demostracion se entra sin completar nada, igual que antes de agregar Firebase.
        if (!isDemo) {
            val errors = validateSignIn(current.email, current.password)
            if (errors.hasErrors) {
                _form.update { it.copy(errors = errors) }
                return
            }
        }
        runAuth {
            repository.signIn(current.email.trim(), current.password)
            AuthState.Authenticated
        }
    }

    fun signUp() {
        val current = _form.value
        if (!isDemo) {
            val errors = validateSignUp(current.fullName, current.email, current.password)
            if (errors.hasErrors) {
                _form.update { it.copy(errors = errors) }
                return
            }
        }
        runAuth {
            repository.signUp(current.email.trim(), current.password)
            AuthState.Authenticated
        }
    }

    fun sendPasswordReset() {
        val email = _form.value.email.trim()
        val error = emailError(email)
        if (error != null) {
            _form.update { it.copy(errors = it.errors.copy(email = error)) }
            return
        }
        runAuth {
            repository.sendPasswordReset(email)
            AuthState.PasswordResetSent(email)
        }
    }

    fun signOut() {
        repository.signOut()
        _form.value = AuthFormState() // no deja el email ni la contraseña del usuario anterior
        _authState.value = AuthState.Unauthenticated
    }

    /** La pantalla ya mostro el mensaje (error o email de reseteo enviado): vuelve al reposo. */
    fun messageShown() {
        _authState.update { current ->
            if (current is AuthState.Error || current is AuthState.PasswordResetSent) {
                AuthState.Unauthenticated
            } else {
                current
            }
        }
    }

    /**
     * Pone el estado en Loading, ejecuta la operacion en una coroutine y guarda el resultado.
     * Si el repositorio lanza AuthException, el estado pasa a Error con su mensaje en español.
     */
    private fun runAuth(operation: suspend () -> AuthState) {
        if (_authState.value == AuthState.Loading) return // evita un doble toque en el boton
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            _authState.value = try {
                operation()
            } catch (e: AuthException) {
                AuthState.Error(e.message ?: "No se pudo completar la operación.")
            }
        }
    }
}