package com.example.parcialtp3.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.R

/**
 * Manrope en toda la app (Regular 400, Medium 500, SemiBold 600, Bold 700), como indica la
 * pagina "Tipografia" del Figma. Los .ttf estan en res/font (licencia SIL OFL).
 */
val ManropeFamily = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
)

private fun vora(size: Int, weight: FontWeight, lineHeight: Int, letterSpacing: TextUnit = 0.sp) = TextStyle(
    fontFamily = ManropeFamily,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = letterSpacing,
)

/**
 * Escala del diseno (tamanos habituales 10-24 px):
 *  - cifra destacada 24 SemiBold · titulo de pantalla 20 Bold · titulo de tarjeta 18 SemiBold
 *  - accion 13-14 SemiBold · cuerpo 13/18 Regular · etiqueta 10 SemiBold
 */
val Typography = Typography(
    displayLarge = vora(40, FontWeight.SemiBold, 48),
    displayMedium = vora(36, FontWeight.SemiBold, 44),
    displaySmall = vora(32, FontWeight.SemiBold, 40),
    headlineLarge = vora(28, FontWeight.SemiBold, 36),
    headlineMedium = vora(24, FontWeight.SemiBold, 32),
    headlineSmall = vora(22, FontWeight.SemiBold, 28),
    titleLarge = vora(20, FontWeight.Bold, 26),
    titleMedium = vora(18, FontWeight.SemiBold, 24),
    titleSmall = vora(14, FontWeight.SemiBold, 20),
    bodyLarge = vora(15, FontWeight.Normal, 22),
    bodyMedium = vora(13, FontWeight.Normal, 18),
    bodySmall = vora(12, FontWeight.Normal, 16),
    labelLarge = vora(14, FontWeight.SemiBold, 20),
    labelMedium = vora(13, FontWeight.SemiBold, 18),
    labelSmall = vora(10, FontWeight.SemiBold, 14, 0.5.sp),
)
