package com.example.parcialtp3.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * Paleta de VORA Coach, tomada de la pagina "Sistema de diseno - VORA / Colores" del Figma.
 * Los nombres siguen la nomenclatura sugerida alli (color / marca / principal, etc.).
 */

// Identidad y acentos
val VoraGreen = Color(0xFF123D35)   // color / marca / principal: acciones, paneles, progreso
val VoraMint = Color(0xFF65D6B0)    // color / acento / menta: coach, voz, carbohidratos
val VoraLime = Color(0xFFC9E86A)    // color / acento / lima: CTA destacado, grasas

// Superficies
val VoraSurface = Color(0xFFFFFFFF)     // tarjetas y campos
val VoraBackground = Color(0xFFF7F8F5)  // base de pantalla
val VoraBorder = Color(0xFFE4E9E6)      // separadores y bordes

// Texto (tres niveles)
val VoraTextPrimary = Color(0xFF17211F)
val VoraTextSecondary = Color(0xFF68736F)
val VoraTextMuted = Color(0xFF9AA39F)

// Senales
val VoraError = Color(0xFFD95D5D)    // error / atencion (tambien "NOT STARTED")
val VoraSuccess = Color(0xFF27AE60)

// Menta translucida (12,16 %) para mensajes de apoyo. Version opaca sobre VoraBackground,
// usada como "container" de Material porque algunos componentes no admiten alfa.
val VoraMintTint = Color(0xFFE5F4ED)
