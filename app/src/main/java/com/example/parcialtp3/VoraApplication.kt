package com.example.parcialtp3

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Punto de entrada de Hilt (RF08): @HiltAndroidApp genera el contenedor de dependencias de toda
 * la app. Desde aca Hilt construye Retrofit, Room, los repositorios y los ViewModels.
 */
@HiltAndroidApp
class VoraApplication : Application()
