package com.example.parcialtp3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.parcialtp3.data.repository.AuthRepository
import com.example.parcialtp3.navigation.AppDestination
import com.example.parcialtp3.navigation.AppNavHost
import com.example.parcialtp3.ui.theme.ParcialTP3Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// Solo tema + grafo de navegacion
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Hilt completa este campo dentro de super.onCreate(), antes de que lo usemos.
    // Es el mismo AuthRepository (singleton) que reciben los ViewModels.
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Firebase recuerda la sesion en el dispositivo: si no se cerro, se entra directo al Home
        // y el login ni se dibuja (tampoco queda en la pila, asi que atras no vuelve a el).
        val startDestination =
            if (authRepository.currentUserId != null) AppDestination.HomeFlow.route
            else AppDestination.LoginFlow.route

        setContent {
            ParcialTP3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}