package com.example.parcialtp3.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.parcialtp3.shared.VoraAvatar
import com.example.parcialtp3.shared.VoraCard
import com.example.parcialtp3.shared.VoraDarkChip
import com.example.parcialtp3.shared.VoraDarkPanel
import com.example.parcialtp3.shared.VoraIconButton
import com.example.parcialtp3.shared.VoraLimeButton
import com.example.parcialtp3.shared.VoraOrb
import com.example.parcialtp3.shared.VoraOverline
import com.example.parcialtp3.shared.VoraProgressBar
import com.example.parcialtp3.shared.VoraRing
import com.example.parcialtp3.shared.VoraScreen
import com.example.parcialtp3.shared.VoraSectionTitle
import com.example.parcialtp3.shared.VoraTextLink
import com.example.parcialtp3.shared.VoraTipCard
import com.example.parcialtp3.shared.VoraTitle
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraMint
import com.example.parcialtp3.ui.theme.VoraTextMuted

/*
 * Home flow. Pantallas solo visuales (datos de ejemplo tomados de Figma), sin logica.
 * Las lambdas son el camino de navegacion: quien conecte datos reales no debe tocarlas.
 */

/** Home / 01 — Dashboard. */
@Composable
fun HomeDashboardScreen(
    onNotificationsClick: () -> Unit,
    onStartVoiceClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onLogBreakfastClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        // Saludo + campana
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraAvatar("AM", size = 52.dp)
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text("Good morning", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Alex Morgan", style = MaterialTheme.typography.titleMedium)
            }
            VoraIconButton("🔔", onNotificationsClick)
        }

        // Panel del coach
        VoraDarkPanel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 30.dp)
                Text(
                    "VORA Coach",
                    color = VoraMint,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                )
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.14f)) {
                    Text(
                        "AI ONLINE",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
            }
            Text(
                "\"How can I support your fitness targets today?\"",
                color = Color.White,
                fontSize = 22.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                "Tap the mic to talk naturally. I'll plan or log for you.",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium,
            )
            VoraLimeButton("🎙  Start voice session", onStartVoiceClick)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VoraDarkChip("Start my workout", onStartWorkoutClick)
                VoraDarkChip("Log breakfast", onLogBreakfastClick)
            }
        }

        // Mensaje del coach
        VoraTipCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 16.sp)
                Text(
                    "You're on track. A protein-rich dinner would help you reach your daily target.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
        }

        VoraSectionTitle("Today's Overview")

        // Anillo + macros
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.height(IntrinsicSize.Min)) {
            VoraCard(modifier = Modifier.width(120.dp).fillMaxHeight()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    VoraRing(0.72f, "72%")
                    Spacer(Modifier.height(8.dp))
                    Text("Daily Goal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            VoraCard(modifier = Modifier.weight(1f).fillMaxHeight()) {
                MacroRow("Nutrition", "1,420", " / 2,100 kcal")
                Spacer(Modifier.height(6.dp))
                MacroRow("Protein", "104g", " / 140g")
                VoraProgressBar(104f / 140f, color = VoraGreen)
                Spacer(Modifier.height(6.dp))
                MacroRow("Carbs", "152g", " / 220g")
                VoraProgressBar(152f / 220f, color = VoraMint)
            }
        }

        // Pasos / distancia / kcal activas
        VoraCard {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Stat("STEPS", "7,842", Modifier.weight(1f))
                Divider()
                Stat("DISTANCE", "5.6 km", Modifier.weight(1f).padding(start = 12.dp))
                Divider()
                Stat("ACTIVE KCAL", "312 kcal", Modifier.weight(1f).padding(start = 12.dp))
            }
        }

        // Entrenamiento recomendado
        VoraCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOverline("📅 Recommended workout", Modifier.weight(1f))
                VoraOverline("Today")
            }
            Text("Upper Body Strength", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 8.dp))
            Text(
                "42 min • 5 exercises • Intermediate",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    "Estimated Vol: 2,340 kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VoraTextMuted,
                    modifier = Modifier.weight(1f),
                )
                Surface(
                    shape = CircleShape,
                    color = VoraGreen,
                    modifier = Modifier.clip(CircleShape).clickable(onClick = onStartWorkoutClick),
                ) {
                    Text(
                        "Start workout",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun MacroRow(label: String, value: String, goal: String) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text(goal, fontSize = 12.sp, color = VoraTextMuted)
    }
}

@Composable
private fun Stat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        VoraOverline(label)
        Text(value, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .padding(start = 12.dp)
            .width(1.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.outlineVariant),
    )
}

/** Home / 02 — Notifications. Cada notificacion abre la pantalla que menciona. */
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onWeeklyReviewClick: () -> Unit,
    onWorkoutPlanClick: () -> Unit,
    onNutritionGoalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraIconButton("←", onBackClick, size = 40.dp)
            VoraTitle("Notifications", modifier = Modifier.weight(1f).padding(start = 12.dp))
            // "Mark all as read" es solo visual: la funcionalidad es de otra etapa.
            VoraTextLink("Mark all as read", onClick = {})
        }

        VoraOverline("Today")
        NotificationCard("✨", "VORA Coach", "2h ago", "Your weekly health review is ready.",
            "We noticed your training consistency improved by 14% over the last week.", unread = true, onClick = onWeeklyReviewClick)
        NotificationCard("🏋", "Workout Plan", "4h ago", "Upper Body Strength is ready.",
            "Equipped for home/gym. Approx. 42 minutes.", unread = true, onClick = onWorkoutPlanClick)
        NotificationCard("🍎", "Nutrition Goal", "6h ago", "Protein target milestone alert.",
            "You require 36g more of protein to hit today's UK performance baseline.", unread = false, onClick = onNutritionGoalClick)

        VoraOverline("Yesterday", modifier = Modifier.padding(top = 8.dp))
        NotificationCard("🔥", "Milestone Reached", "1d ago", "4-day workout streak logged!",
            "Outstanding work, Alex. Let's make it 5 today.", unread = false, onClick = {})
    }
}

@Composable
private fun NotificationCard(
    icon: String,
    source: String,
    time: String,
    title: String,
    body: String,
    unread: Boolean,
    onClick: () -> Unit,
) {
    VoraCard(onClick = onClick) {
        Row {
            Text(icon, fontSize = 18.sp, modifier = Modifier.padding(end = 14.dp, top = 2.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(source, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Text(time, style = MaterialTheme.typography.bodySmall, color = VoraTextMuted)
                    if (unread) {
                        Box(
                            Modifier
                                .padding(start = 6.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(VoraGreen),
                        )
                    }
                }
                Text(title, style = MaterialTheme.typography.titleSmall, fontSize = 15.sp, modifier = Modifier.padding(top = 2.dp))
                Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}
