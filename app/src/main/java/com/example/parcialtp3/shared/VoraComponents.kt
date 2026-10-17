package com.example.parcialtp3.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.ui.theme.VoraBorder
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraLime
import com.example.parcialtp3.ui.theme.VoraMint
import com.example.parcialtp3.ui.theme.VoraMintTint
import com.example.parcialtp3.ui.theme.VoraTextMuted

/*
 * Componentes visuales compartidos de VORA, sacados del Sistema de diseno de Figma.
 * Todas las pantallas se arman con estas piezas para verse iguales entre si.
 * Son solo visuales: no tienen logica, reciben lambdas.
 */

/** Pantalla con scroll, fondo VORA y margen lateral de 20 px. */
@Composable
fun VoraScreen(
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.background,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content,
    )
}

/** Tarjeta blanca: radio 16, borde #E4E9E6, padding 16. */
@Composable
fun VoraCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, VoraBorder),
        modifier = modifier.fillMaxWidth().let { if (onClick != null) it.clip(shape).clickable(onClick = onClick) else it },
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = content,
        )
    }
}

/** Panel verde oscuro destacado (radio 20, padding 20) para el coach y el entrenamiento. */
@Composable
fun VoraDarkPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = VoraGreen,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}

/** Tarjeta de apoyo en menta tenue (mensajes del coach). Radio 12, padding 12. */
@Composable
fun VoraTipCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = VoraMintTint,
        border = BorderStroke(1.dp, VoraMint.copy(alpha = 0.4f)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(12.dp), content = content)
    }
}

/** Boton principal: verde, texto blanco 15 Bold, padding 14/24, radio 100. */
@Composable
fun VoraPrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
        modifier = modifier.fillMaxWidth(),
    ) { Text(text, fontSize = 15.sp, fontWeight = FontWeight.Bold) }
}

/** CTA lima con texto verde (sobre paneles oscuros y en Login). */
@Composable
fun VoraLimeButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = VoraLime, contentColor = VoraGreen),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
        modifier = modifier.fillMaxWidth(),
    ) { Text(text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold) }
}

/** Boton secundario: pildora blanca, borde verde fino, texto oscuro. */
@Composable
fun VoraOutlinedButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(1.dp, VoraGreen),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = modifier.fillMaxWidth(),
    ) { Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold) }
}

/** Chip chico sobre panel oscuro: fondo blanco translucido, texto blanco 13. */
@Composable
fun VoraDarkChip(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.12f),
        modifier = modifier.clip(CircleShape).clickable(onClick = onClick),
    ) {
        Text(
            text,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        )
    }
}

/** Chip blanco con borde (prompts de voz, filtros). */
@Composable
fun VoraChip(text: String, onClick: () -> Unit = {}, modifier: Modifier = Modifier, center: Boolean = false) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, VoraBorder),
        modifier = modifier.clip(CircleShape).clickable(onClick = onClick),
    ) {
        Text(
            text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = if (center) androidx.compose.ui.text.style.TextAlign.Center else androidx.compose.ui.text.style.TextAlign.Start,
            modifier = (if (center) Modifier.fillMaxWidth() else Modifier).padding(horizontal = 14.dp, vertical = 12.dp),
        )
    }
}

/** Boton de texto (View History / Mark all as read): verde SemiBold sin fondo. */
@Composable
fun VoraTextLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, muted: Boolean = false) {
    Text(
        text,
        color = if (muted) VoraTextMuted else VoraGreen,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.clickable(onClick = onClick).padding(vertical = 4.dp),
    )
}

/** Etiqueta chica en mayusculas (TODAY, STEPS...): 10-11 SemiBold gris. */
@Composable
fun VoraOverline(text: String, modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onSurfaceVariant) {
    Text(
        text.uppercase(),
        color = color,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp,
        modifier = modifier,
    )
}

/** Titulo de pantalla (20-24 Bold/SemiBold). */
@Composable
fun VoraTitle(text: String, modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onBackground) {
    Text(text, style = MaterialTheme.typography.headlineMedium, color = color, modifier = modifier)
}

/** Titulo de seccion (18 SemiBold). */
@Composable
fun VoraSectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(text, style = MaterialTheme.typography.titleMedium, modifier = modifier)
}

/** Barra de progreso de 4-6 px con fondo gris claro. */
@Composable
fun VoraProgressBar(progress: Float, modifier: Modifier = Modifier, color: Color = VoraGreen, height: Dp = 6.dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(VoraBorder),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(height)
                .clip(CircleShape)
                .background(color),
        )
    }
}

/** Anillo de progreso con el porcentaje al centro (72 px en Figma). */
@Composable
fun VoraRing(progress: Float, label: String, modifier: Modifier = Modifier, size: Dp = 72.dp, color: Color = VoraGreen) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .drawBehind {
                val stroke = 6.dp.toPx()
                val inset = stroke / 2
                val arc = Size(this.size.width - stroke, this.size.height - stroke)
                drawArc(VoraBorder, 0f, 360f, false, Offset(inset, inset), arc, style = Stroke(stroke))
                drawArc(color, -90f, 360f * progress, false, Offset(inset, inset), arc, style = Stroke(stroke, cap = StrokeCap.Round))
            },
    ) {
        Text(label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

/** Avatar circular provisorio con iniciales (en Figma es una foto). */
@Composable
fun VoraAvatar(initials: String, modifier: Modifier = Modifier, size: Dp = 48.dp) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(VoraMint.copy(alpha = 0.35f))
            .border(1.dp, VoraBorder, CircleShape),
    ) {
        Text(initials, fontSize = (size.value * 0.34f).sp, fontWeight = FontWeight.Bold, color = VoraGreen)
    }
}

/** Circulo blanco con borde para iconos de accion (campana, volver). El icono es un emoji provisorio. */
@Composable
fun VoraIconButton(icon: String, onClick: () -> Unit, modifier: Modifier = Modifier, size: Dp = 44.dp) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, VoraBorder),
        modifier = modifier.size(size).clip(CircleShape).clickable(onClick = onClick),
    ) {
        Box(contentAlignment = Alignment.Center) { Text(icon, fontSize = 18.sp) }
    }
}

/** Orbe verde del coach (circulo con degradado simple). */
@Composable
fun VoraOrb(modifier: Modifier = Modifier, size: Dp = 28.dp) {
    // El degradado no depende de nada: se crea una sola vez en vez de en cada recomposicion.
    val brush = androidx.compose.runtime.remember {
        androidx.compose.ui.graphics.Brush.radialGradient(listOf(VoraMint, Color(0xFF1F6B57), VoraGreen))
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(brush),
    )
}

/**
 * Campo de formulario de Figma: etiqueta arriba, campo blanco de radio 12 con borde suave,
 * padding 12/16. Mantiene su propio texto solo para poder escribir en la demo; no valida nada.
 */
@Composable
fun VoraTextField(
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    leading: String? = null,
    trailing: String? = null,
    initialValue: String = "",
    filled: Boolean = false,
    labelUppercase: Boolean = false,
) {
    var value by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf(initialValue) }
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (labelUppercase) VoraOverline(label) else Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        androidx.compose.material3.OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            singleLine = true,
            placeholder = { Text(placeholder, color = VoraTextMuted, fontSize = 14.sp) },
            leadingIcon = leading?.let { { Text(it, fontSize = 16.sp) } },
            trailingIcon = trailing?.let { { Text(it, fontSize = 16.sp) } },
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedContainerColor = if (filled) VoraMintTint else MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = if (filled) VoraMintTint else MaterialTheme.colorScheme.surface,
                focusedBorderColor = VoraGreen,
                unfocusedBorderColor = if (filled) VoraBorder else VoraTextMuted,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** Fondo con degradado menta suave arriba, como en Login. */
@Composable
fun VoraSoftBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val bg = MaterialTheme.colorScheme.background
    val brush = androidx.compose.runtime.remember(bg) {
        androidx.compose.ui.graphics.Brush.verticalGradient(listOf(VoraMintTint, bg), endY = 900f)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush),
    ) { content() }
}

/** Barra superior de Figma: boton circular de volver + titulo + contenido opcional a la derecha. */
@Composable
fun VoraAppBar(
    title: String,
    onBackClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    trailing: @Composable (() -> Unit)? = null,
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBackClick != null) {
            Surface(
                shape = CircleShape,
                color = if (dark) Color.White.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                border = if (dark) null else BorderStroke(1.dp, VoraBorder),
                modifier = Modifier.size(40.dp).clip(CircleShape).clickable(onClick = onBackClick),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("‹", fontSize = 24.sp, color = if (dark) Color.White else VoraGreen)
                }
            }
            androidx.compose.foundation.layout.Spacer(Modifier.size(12.dp))
        }
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = if (dark) Color.White else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        trailing?.invoke()
    }
}

/** Pildora chica de estado (ONLINE, SPEAKING...). */
@Composable
fun VoraStatusPill(text: String, modifier: Modifier = Modifier, dark: Boolean = false) {
    Surface(
        shape = CircleShape,
        color = if (dark) VoraMint.copy(alpha = 0.18f) else VoraMintTint,
        modifier = modifier,
    ) {
        Text(
            text,
            color = if (dark) VoraMint else VoraGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

/** Orbe grande del asistente de voz: anillo exterior + degradado + barras de audio. */
@Composable
fun VoraBigOrb(modifier: Modifier = Modifier, size: Dp = 200.dp, grey: Boolean = false, glyph: String = "▐▌▐▌▐") {
    val core = if (grey) listOf(Color(0xFFB9BDBB), Color(0xFF6E7471), Color(0xFF3E4442)) else listOf(VoraMint, Color(0xFF1F6B57), VoraGreen)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .border(2.dp, (if (grey) Color(0xFF9AA39F) else VoraMint).copy(alpha = 0.35f), CircleShape),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size * 0.78f)
                .clip(CircleShape)
                .border(4.dp, VoraLime.copy(alpha = 0.7f), CircleShape)
                .background(androidx.compose.ui.graphics.Brush.radialGradient(core)),
        ) {
            Text(if (grey) "↻" else "||| |||", color = Color.White, fontSize = if (grey) 36.sp else 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

/** Tarjeta de texto sobre fondo oscuro (transcripcion en vivo, respuesta de VORA). */
@Composable
fun VoraDarkCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        modifier = modifier.fillMaxWidth(),
    ) { Column(modifier = Modifier.padding(20.dp), content = content) }
}

/** Boton circular grande de microfono (verde oscuro o menta). */
@Composable
fun VoraMicButton(onClick: () -> Unit, modifier: Modifier = Modifier, mint: Boolean = false, size: Dp = 76.dp) {
    Surface(
        shape = CircleShape,
        color = if (mint) VoraMint else VoraGreen,
        modifier = modifier.size(size).clip(CircleShape).clickable(onClick = onClick),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("🎙", fontSize = (size.value * 0.34f).sp)
        }
    }
}

/**
 * Dialogo de demostracion: los controles que Figma muestra pero que no tienen pantalla propia
 * (editar, duplicar, ayuda, ajustes...) abren este aviso en vez de quedar sin respuesta (NAV03).
 */
class DemoDialogState {
    var message: String? by androidx.compose.runtime.mutableStateOf(null)
    fun show(text: String) { message = text }
}

@Composable
fun rememberDemoDialog(): DemoDialogState = androidx.compose.runtime.remember { DemoDialogState() }

@Composable
fun DemoDialogHost(state: DemoDialogState, title: String = "Demostración") {
    val text = state.message
    if (text != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { state.message = null },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = { state.message = null }) {
                    Text("OK", color = VoraGreen, fontWeight = FontWeight.SemiBold)
                }
            },
            title = { Text(title, style = MaterialTheme.typography.titleMedium) },
            text = { Text(text, style = MaterialTheme.typography.bodyMedium) },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp),
        )
    }
}
