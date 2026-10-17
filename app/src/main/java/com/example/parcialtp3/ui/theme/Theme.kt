package com.example.parcialtp3.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

/** Esquema de color de VORA: verde profundo, acentos menta y lima, superficies tranquilas. */
private val VoraColorScheme = lightColorScheme(
    primary = VoraGreen,
    onPrimary = Color.White,
    primaryContainer = VoraLime,            // CTA destacado (lima) con texto verde
    onPrimaryContainer = VoraGreen,
    secondary = VoraMint,
    onSecondary = VoraGreen,                // texto verde sobre menta o lima (criterio de uso)
    secondaryContainer = VoraMintTint,      // mensajes de apoyo y item activo
    onSecondaryContainer = VoraGreen,
    tertiary = VoraLime,
    onTertiary = VoraGreen,
    background = VoraBackground,
    onBackground = VoraTextPrimary,
    surface = VoraSurface,
    onSurface = VoraTextPrimary,
    surfaceVariant = VoraBackground,
    onSurfaceVariant = VoraTextSecondary,
    outline = VoraTextMuted,
    outlineVariant = VoraBorder,
    error = VoraError,
    onError = Color.White,
    // Tarjetas, campos y barras en blanco sobre el fondo casi blanco de la pantalla.
    surfaceContainerLowest = VoraSurface,
    surfaceContainerLow = VoraSurface,
    surfaceContainer = VoraSurface,
    surfaceContainerHigh = VoraSurface,
    surfaceContainerHighest = VoraSurface,
)

/** Radios del diseno: 8 / 12 / 16 / 20 / 24 px (la pildora de 100 px se usa con CircleShape). */
private val VoraShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),   // campos y mensajes
    medium = RoundedCornerShape(16.dp),  // tarjetas
    large = RoundedCornerShape(20.dp),   // paneles destacados
    extraLarge = RoundedCornerShape(24.dp),
)

/**
 * Tema de la app. Siempre claro (el diseno de Figma no tiene modo oscuro) y sin color dinamico,
 * para que la app se vea igual en cualquier celular. Los parametros se conservan por compatibilidad.
 */
@Composable
fun ParcialTP3Theme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = false,
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        // Iconos oscuros en la barra de estado y de navegacion: el fondo es claro aunque el
        // celular este en modo oscuro.
        SideEffect {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = true
            controller.isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = VoraColorScheme,
        typography = Typography,
        shapes = VoraShapes,
        content = content,
    )
}
