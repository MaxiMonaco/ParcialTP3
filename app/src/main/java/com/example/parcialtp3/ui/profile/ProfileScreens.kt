package com.example.parcialtp3.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.shared.VoraAppBar
import com.example.parcialtp3.shared.VoraAvatar
import com.example.parcialtp3.shared.DemoDialogHost
import com.example.parcialtp3.shared.rememberDemoDialog
import com.example.parcialtp3.shared.VoraCard
import com.example.parcialtp3.shared.VoraDarkPanel
import com.example.parcialtp3.shared.VoraIconButton
import com.example.parcialtp3.shared.VoraLimeButton
import com.example.parcialtp3.shared.VoraOrb
import com.example.parcialtp3.shared.VoraOverline
import com.example.parcialtp3.shared.VoraPrimaryButton
import com.example.parcialtp3.shared.VoraScreen
import com.example.parcialtp3.shared.VoraTextField
import com.example.parcialtp3.shared.VoraTipCard
import com.example.parcialtp3.shared.VoraTitle
import com.example.parcialtp3.ui.theme.VoraBorder
import com.example.parcialtp3.ui.theme.VoraError
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraLime
import com.example.parcialtp3.ui.theme.VoraMint
import com.example.parcialtp3.ui.theme.VoraMintTint
import com.example.parcialtp3.ui.theme.VoraTextMuted

/*
 * Profile Flow (solo visual, datos de ejemplo de Figma). Los chips, tarjetas de seleccion e
 * interruptores se ven como en el diseno pero todavia no guardan nada.
 */

@Composable
private fun SectionLabel(text: String) {
    VoraOverline(text, modifier = Modifier.padding(top = 4.dp))
}

@Composable
private fun SettingRow(icon: String, title: String, sub: String?, onClick: () -> Unit, badge: String? = null) {
    VoraCard(onClick = onClick, contentPadding = 14.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.background, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(icon, fontSize = 17.sp) }
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
                if (sub != null) Text(sub, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (badge != null) {
                Surface(shape = CircleShape, color = VoraGreen, modifier = Modifier.padding(end = 8.dp)) {
                    Text(badge, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
            }
            Text("›", color = VoraTextMuted, fontSize = 20.sp)
        }
    }
}

/** Tarjeta de seleccion (objetivo, estilo): verde menta con check cuando esta elegida. */
@Composable
private fun SelectCard(title: String, sub: String?, selected: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) VoraMintTint else MaterialTheme.colorScheme.surface,
        border = if (selected) BorderStroke(2.dp, VoraMint) else BorderStroke(1.dp, VoraBorder),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontSize = 17.sp)
                if (sub != null) Text(sub, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
            if (selected) {
                Surface(shape = CircleShape, color = VoraGreen, modifier = Modifier.size(26.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("✓", color = Color.White, fontSize = 14.sp) }
                }
            } else {
                Box(Modifier.size(26.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface).padding(1.dp).background(VoraBorder, CircleShape).padding(1.5.dp).background(MaterialTheme.colorScheme.surface, CircleShape))
            }
        }
    }
}

/** Chip de seleccion: verde menta (activo), verde oscuro (frecuencia) o blanco. */
@Composable
private fun OptionChip(text: String, state: Int = 0, suffix: String? = null, suffixColor: Color = VoraGreen) {
    // state: 0 = sin elegir, 1 = elegido menta, 2 = elegido verde oscuro, 3 = con borde menta (alimentos)
    val bg = when (state) { 1 -> VoraMint; 2 -> VoraGreen; else -> MaterialTheme.colorScheme.surface }
    val fg = if (state == 2) Color.White else if (state == 1) VoraGreen else MaterialTheme.colorScheme.onSurface
    val border = when (state) { 3 -> BorderStroke(1.dp, VoraMint); 1, 2 -> null; else -> BorderStroke(1.dp, VoraBorder) }
    Surface(shape = CircleShape, color = bg, border = border) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text, color = fg, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            if (suffix != null) Text("  $suffix", color = suffixColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

/** Profile / 01 — Dashboard. */
@Composable
fun ProfileDashboardScreen(
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val demo = rememberDemoDialog()
    VoraScreen(modifier = modifier) {
        DemoDialogHost(demo)
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraTitle("Profile", Modifier.weight(1f))
            VoraIconButton("⚙", onSettingsClick, size = 40.dp)
        }
        VoraCard {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                VoraAvatar("AM", size = 84.dp)
                Text("Alex Morgan", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 12.dp))
                Text("Building strength & improving fitness", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.background, modifier = Modifier.padding(top = 14.dp).clip(CircleShape).clickable(onClick = onEditProfileClick)) {
                    Text("Edit profile", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = VoraGreen, modifier = Modifier.padding(horizontal = 20.dp, vertical = 9.dp))
                }
            }
        }
        VoraCard {
            VoraOverline("My snapshot")
            listOf("Goal" to "Build strength & improve fitness", "Experience" to "Intermediate", "Training" to "4 days/week", "Preferred Workout" to "Strength training").forEach { (k, v) ->
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text(k, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Text(v, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        VoraDarkPanel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 28.dp)
                Text("VORA Personalisation", color = VoraMint, fontSize = 15.sp, modifier = Modifier.padding(start = 10.dp))
            }
            Text("VORA knows your goals", color = Color.White, style = MaterialTheme.typography.titleSmall, fontSize = 17.sp)
            Text("Your coaching adapts to your training, nutrition and activity preferences.", color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.bodyMedium)
            Surface(shape = CircleShape, color = VoraLime, modifier = Modifier.clip(CircleShape).clickable(onClick = onGoalsClick)) {
                Text("Review preferences", color = VoraGreen, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp))
            }
        }
        SettingRow("◎", "Your Goals", "Build strength & improve fitness", onGoalsClick)
        SettingRow("☰", "Your Preferences", "Training, nutrition and coaching", onPreferencesClick)
        // "Connected Data" y "Account" no tienen pantalla propia en Figma: solo visuales.
        SettingRow("❤", "Connected Data", "Health & activity data", onClick = { demo.show("Los datos de salud conectados son de demostración.") }, badge = "Synced")
        SettingRow("🔒", "Account", "Subscription, notifications & privacy", onClick = { demo.show("La cuenta no tiene pantalla propia en el diseño.") })
    }
}

/** Profile / 02 — Edit Profile. */
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveChangesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        VoraAppBar("Edit profile", onBackClick)
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            VoraAvatar("AM", size = 84.dp)
            Text("Change photo", color = VoraGreen, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 8.dp))
        }
        VoraTextField("First name", "", initialValue = "Alex", labelUppercase = true)
        VoraTextField("Last name", "", initialValue = "Morgan", labelUppercase = true)
        VoraTextField("Date of birth", "", initialValue = "14 March 1995", labelUppercase = true)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            VoraTextField("Height", "", initialValue = "178 cm", labelUppercase = true, modifier = Modifier.weight(1f))
            VoraTextField("Weight", "", initialValue = "76 kg", labelUppercase = true, modifier = Modifier.weight(1f))
        }
        VoraTextField("Gender", "", initialValue = "Prefer not to say", labelUppercase = true)
        VoraTipCard {
            Text("ⓘ  Used to personalise your recommendations.", style = MaterialTheme.typography.bodyMedium)
        }
        VoraPrimaryButton("Save changes", onSaveChangesClick)
        Text(
            "Your information is used to personalise your VORA experience.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** Profile / 03 — Goals & Preferences ("Your goals"). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalsPreferencesScreen(
    onBackClick: () -> Unit,
    onSavePreferencesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        VoraAppBar("Your goals", onBackClick)
        Text("Tell VORA what you're working towards. Your coaching adapts around this.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        SectionLabel("Primary goal — what's your main goal?")
        SelectCard("Build strength", "Focus on weight training & mechanical load", true)
        SelectCard("Improve fitness", "Condition cardiovascular & energy systems", true)
        SelectCard("Lose body fat", null, false)
        SelectCard("Build healthy habits", null, false)
        SectionLabel("Training frequency")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OptionChip("3 days"); OptionChip("4 days", 2); OptionChip("5 days"); OptionChip("6+ days")
        }
        SectionLabel("Workout preference")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OptionChip("Strength", 1); OptionChip("Cardio"); OptionChip("HIIT"); OptionChip("Mobility"); OptionChip("Mixed")
        }
        SectionLabel("Coaching style — how should VORA coach you?")
        SelectCard("Balanced", "Practical guidance with gentle motivation", true)
        VoraPrimaryButton("Save preferences", onSavePreferencesClick)
    }
}

/** Profile / 04 — Nutrition Preferences. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutritionPreferencesScreen(
    onBackClick: () -> Unit,
    onSavePreferencesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        VoraAppBar("Nutrition preferences", onBackClick)
        Text("Help VORA make food recommendations that actually fit your lifestyle.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        SectionLabel("Dietary preference")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OptionChip("Everything", 1); OptionChip("Vegetarian"); OptionChip("Vegan"); OptionChip("Pescatarian"); OptionChip("Other")
        }
        SectionLabel("Foods you enjoy")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Chicken", "Eggs", "Fish", "Rice", "Pasta", "Greek yoghurt", "Vegetables", "Fruit").forEach { OptionChip(it, 3, "✓") }
        }
        SectionLabel("Foods to avoid")
        VoraTextField("", "+  Add an ingredient...")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Peanuts", "Shellfish", "Mushrooms").forEach { OptionChip(it, 0, "⊗", VoraError) }
        }
        SectionLabel("Meal preferences")
        VoraCard(contentPadding = 14.dp) {
            listOf("Meals per day" to "3 meals + 1 snack", "Preferred breakfast" to "Quick & high protein", "Preferred dinner" to "Balanced").forEach { (k, v) ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(k, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Text(v, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
        SectionLabel("What matters most to you?")
        SelectCard("Hit protein targets", null, true)
        SelectCard("Eat more consistently", null, true)
        VoraPrimaryButton("Save preferences", onSavePreferencesClick)
    }
}

@Composable
private fun ToggleRow(label: String, on: Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(
            checked = on,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(checkedTrackColor = VoraMint, checkedThumbColor = Color.White, checkedBorderColor = VoraMint),
        )
    }
}

/** Profile / 05 — Voice & AI Settings ("VORA Coach"). */
@Composable
fun VoiceAiSettingsScreen(
    onBackClick: () -> Unit,
    onPrivacyAndPermissionsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        VoraAppBar("VORA Coach", onBackClick)
        Text("Choose how VORA communicates and coaches you.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        SectionLabel("Coaching style")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Supportive", "Balanced", "Direct").forEach { t ->
                val sel = t == "Balanced"
                Surface(shape = RoundedCornerShape(12.dp), color = if (sel) VoraGreen else MaterialTheme.colorScheme.surface, border = if (sel) null else BorderStroke(1.dp, VoraBorder), modifier = Modifier.weight(1f)) {
                    Text(t, color = if (sel) Color.White else MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 13.dp))
                }
            }
        }
        VoraCard {
            VoraOverline("Active voice")
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Surface(shape = CircleShape, color = VoraGreen, modifier = Modifier.size(34.dp)) { Box(contentAlignment = Alignment.Center) { Text("🔊", fontSize = 14.sp) } }
                Text("VORA Voice 01", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f).padding(start = 12.dp))
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, VoraBorder)) {
                    Text("▶ Preview", fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                }
            }
        }
        SectionLabel("Speaking behaviour")
        VoraCard(contentPadding = 14.dp) {
            ToggleRow("Speak responses aloud")
            ToggleRow("Auto-play coaching")
            ToggleRow("Allow interruptions")
            Text("Interrupt VORA anytime while it's speaking.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        SectionLabel("Response style")
        SelectCard("Concise", "Quick answers", false)
        SelectCard("Balanced", "Useful detail without overwhelming", true)
        SelectCard("Detailed", "More explanation and context", false)
        VoraCard(contentPadding = 14.dp) {
            ToggleRow("Voice feedback")
            ToggleRow("Haptic feedback")
        }
        VoraTipCard {
            Text("Your control matters", style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
            Text("Manage microphone permissions and connected data from Settings.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 6.dp))
            Text(
                "Privacy & permissions",
                color = VoraGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                modifier = Modifier.padding(top = 8.dp).clickable(onClick = onPrivacyAndPermissionsClick),
            )
        }
    }
}

/** Profile / 06 — Settings. */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onVoiceAiClick: () -> Unit,
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val demo = rememberDemoDialog()
    VoraScreen(modifier = modifier) {
        DemoDialogHost(demo)
        VoraAppBar("Settings", onBackClick)
        SectionLabel("Account")
        SettingRow("👤", "Alex Morgan", "alex@morgan.com", onClick = { demo.show("Los datos de la cuenta son de ejemplo.") })
        SettingRow("☆", "VORA Premium", "Renews 14 June 2026", onClick = { demo.show("La suscripción es de demostración.") })
        SettingRow("✎", "Manage account", null, onClick = { demo.show("Administrar la cuenta no está incluido en esta versión.") })
        SectionLabel("App preferences")
        // Figma no marca que fila lleva a Voice & AI: la dejamos como fila propia (supuesto del grupo).
        SettingRow("🎙", "Voice & AI", "VORA Coach voice and coaching", onVoiceAiClick)
        SettingRow("🔔", "Notifications", "Workout and meal reminders", onClick = { demo.show("Los recordatorios no están incluidos en esta versión.") })
        SettingRow("◎", "Units", "Metric (kg, cm, km)", onClick = { demo.show("Solo se muestran unidades métricas.") })
        SettingRow("👁", "Appearance", "System default", onClick = { demo.show("La app usa solo el tema claro de VORA.") })
        SectionLabel("Connected data")
        SettingRow("❤", "Health & Activity", "Apple Watch & Health connected", onClick = { demo.show("La conexión con Apple Health es de demostración.") })
        SectionLabel("Privacy & security")
        SettingRow("🔒", "Privacy Policy", null, onClick = { demo.show("La política de privacidad no está incluida en esta demostración.") })
        SettingRow("🛡", "Permissions & Data Controls", null, onClick = { demo.show("Los permisos reales se piden desde el sistema Android.") })
        SectionLabel("Support")
        SettingRow("?", "Help Centre", null, onClick = { demo.show("El centro de ayuda no está incluido en esta demostración.") })
        SettingRow("✉", "Contact Support", null, onClick = { demo.show("El soporte no está disponible en esta demostración.") })
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, VoraError),
            modifier = Modifier.fillMaxWidth().clip(CircleShape).clickable(onClick = onLogOutClick),
        ) {
            Text("Log out", color = VoraError, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 15.dp))
        }
        Text("VORA v1.0.0", style = MaterialTheme.typography.bodySmall, color = VoraTextMuted, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}
