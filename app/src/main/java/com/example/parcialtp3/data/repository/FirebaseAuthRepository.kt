package com.example.parcialtp3.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Autenticacion real con Firebase Authentication (email y contraseña).
 * Firebase guarda la sesion en el dispositivo: si el usuario no cierra sesion,
 * al volver a abrir la app currentUserId sigue teniendo valor.
 */
class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth, // lo construye Hilt (ver FirebaseModule)
) : AuthRepository {

    override val isDemo = false

    override val currentUserId: String?
        get() = auth.currentUser?.uid

    override val currentUserEmail: String?
        get() = auth.currentUser?.email

    override suspend fun signIn(email: String, password: String) {
        // await() espera la respuesta de Firebase sin bloquear la pantalla.
        runAuth { auth.signInWithEmailAndPassword(email, password).await() }
    }

    override suspend fun signUp(email: String, password: String) {
        // Firebase crea la cuenta y ademas deja la sesion abierta.
        runAuth { auth.createUserWithEmailAndPassword(email, password).await() }
    }

    override suspend fun sendPasswordReset(email: String) {
        runAuth { auth.sendPasswordResetEmail(email).await() }
    }

    override fun signOut() = auth.signOut()

    /** Ejecuta una operacion de Firebase y traduce cualquier error a AuthException en español. */
    private suspend fun runAuth(operation: suspend () -> Unit) {
        try {
            operation()
        } catch (e: CancellationException) {
            throw e // la coroutine se cancelo (por ejemplo, se cerro la pantalla): no es un error
        } catch (e: Exception) {
            throw AuthException(toSpanishMessage(e), e)
        }
    }

    // El orden importa: WeakPassword es un tipo de InvalidCredentials, por eso va primero.
    private fun toSpanishMessage(e: Exception): String = when (e) {
        is FirebaseAuthWeakPasswordException ->
            "La contraseña es muy débil: usá al menos 6 caracteres."
        is FirebaseAuthUserCollisionException ->
            "Ya existe una cuenta con ese email."
        is FirebaseAuthInvalidCredentialsException ->
            if (e.errorCode == "ERROR_INVALID_EMAIL") "El email no es válido."
            else "Email o contraseña incorrectos."
        is FirebaseAuthInvalidUserException ->
            "Email o contraseña incorrectos."
        is FirebaseTooManyRequestsException ->
            "Demasiados intentos. Esperá unos minutos y probá de nuevo."
        is FirebaseNetworkException ->
            "No hay conexión. Revisá internet y probá de nuevo."
        else ->
            "No se pudo completar la operación. Probá de nuevo."
    }
}