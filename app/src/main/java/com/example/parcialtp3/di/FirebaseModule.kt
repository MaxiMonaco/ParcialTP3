package com.example.parcialtp3.di

import android.content.Context
import com.example.parcialtp3.data.repository.AuthRepository
import com.example.parcialtp3.data.repository.DemoAuthRepository
import com.example.parcialtp3.data.repository.FirebaseAuthRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Le enseña a Hilt como construir lo relacionado con Firebase.
 * Cualquier clase que pida un AuthRepository con @Inject recibe el que corresponda.
 */
@Module
@InstallIn(SingletonComponent::class) // vive mientras viva la app
object FirebaseModule {

    // FirebaseAuth no es una clase nuestra (no le podemos poner @Inject), por eso se provee aca.
    // Solo se llega a crear si Firebase esta configurado (ver provideAuthRepository).
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Elige la implementacion: Firebase si esta configurado, demostracion si no.
     * Provider<T> crea el objeto recien cuando se llama a get(): asi, sin google-services.json,
     * nunca se construye FirebaseAuthRepository (que fallaria porque Firebase no esta inicializado).
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        firebase: Provider<FirebaseAuthRepository>,
        demo: Provider<DemoAuthRepository>,
    ): AuthRepository =
        if (isFirebaseConfigured(context)) firebase.get() else demo.get()
}

/**
 * Con google-services.json en app/, el plugin de Google Services inicializa Firebase solo al abrir
 * la app. Si no hay ninguna FirebaseApp, es que el archivo no esta y se usa el modo demostracion.
 */
private fun isFirebaseConfigured(context: Context): Boolean =
    FirebaseApp.getApps(context).isNotEmpty()