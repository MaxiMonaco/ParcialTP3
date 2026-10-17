package com.example.parcialtp3.ui.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.shared.VoraAppBar
import com.example.parcialtp3.shared.VoraCard
import com.example.parcialtp3.shared.VoraDarkPanel
import com.example.parcialtp3.shared.VoraLimeButton
import com.example.parcialtp3.shared.VoraOrb
import com.example.parcialtp3.shared.VoraOverline
import com.example.parcialtp3.shared.VoraPrimaryButton
import com.example.parcialtp3.shared.VoraRing
import com.example.parcialtp3.shared.VoraScreen
import com.example.parcialtp3.shared.VoraSectionTitle
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
 * Progress Flow (solo visual, datos de ejemplo de Figma). Los graficos son barras dibujadas
 * con valores fijos; los selectores Week/Month/3 Months y los filtros no cambian nada todavia.
 */

private val DarkSub = Color.White.copy(alpha = 0.7f)

@Composable
private fun PeriodTabs() {
    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, VoraBorder), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(4.dp)) {
            listOf("Week", "Month", "3 Months").forEachIndexed { i, t ->
                Surface(shape = CircleShape, color = if (i == 0) VoraGreen else Color.Transparent, modifier = Modifier.weight(1f)) {
                    Text(
                        t,
                        color = if (i == 0) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun MoreButton() {
    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, VoraBorder), modifier = Modifier.size(40.dp)) {
        Box(contentAlignment = Alignment.Center) { Text("⋯", fontSize = 18.sp) }
    }
}

@Composable
private fun VDivider() {
    Box(Modifier.width(1.dp).fillMaxHeight().background(VoraBorder))
}

@Composable
private fun StatTriple(items: List<Pair<String, String>>) {
    VoraCard(contentPadding = 12.dp) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            items.forEachIndexed { i, (label, value) ->
                if (i > 0) VDivider()
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                    VoraOverline(label)
                    Text(value, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun Bars(values: List<Float>, labels: List<String>, highlight: Int = -1, height: Dp = 110.dp, light: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().height(height + 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom,
    ) {
        values.forEachIndexed { i, v ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .width(26.dp)
                        .height((height * v.coerceAtLeast(0.06f)))
                        .clip(RoundedCornerShape(5.dp))
                        .background(if (v <= 0.06f) VoraBorder else if (light && i != highlight) VoraMint else VoraGreen),
                )
                Text(labels[i], fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

/** Progress / 01 — Today's Overview. */
@Composable
fun ProgressOverviewScreen(
    onStartWorkoutClick: () -> Unit,
    onOpenDashboardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        Column {
            VoraOverline("Tuesday, 8 September")
            VoraTitle("Today's Overview")
        }
        // Tocar la tarjeta abre el panel de progreso general (Progress / 02).
        VoraCard(onClick = onOpenDashboardClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    VoraOverline("Daily completion")
                    Text("72%", fontSize = 34.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
                }
                VoraRing(0.72f, "", size = 72.dp)
            }
            Box(Modifier.padding(vertical = 12.dp).fillMaxWidth().height(1.dp).background(VoraBorder))
            listOf(
                Triple(VoraError, "Workout Progress", "Not started " to "0%"),
                Triple(VoraMint, "Nutrition Target", "1,420 / 2,100 kcal " to "78%"),
                Triple(VoraGreen, "Activity Target", "7,842 steps " to "84%"),
                Triple(VoraLime, "Hydration Intake", "1.6 / 2.5 Litres " to "65%"),
            ).forEach { (c, name, v) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                    Box(Modifier.size(9.dp).clip(CircleShape).background(c))
                    Text(name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f).padding(start = 10.dp))
                    Text(v.first, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(v.second, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        VoraDarkPanel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 16.sp)
                Text("VORA ADVICE", color = VoraMint, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 8.dp))
            }
            Text(
                "\"Completing today's intermediate workout and a protein-rich dinner will secure your optimal target recovery.\"",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        VoraSectionTitle("Category Breakdowns")
        VoraCard {
            Row {
                VoraOverline("Workout", Modifier.weight(1f))
                Text("NOT STARTED", color = VoraError, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            Text("Upper Body Strength", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 6.dp))
            Text("42 min • 5 exercises • Intermediate", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Box(Modifier.padding(top = 12.dp)) { VoraPrimaryButton("Start Workout", onStartWorkoutClick) }
        }
        VoraCard(contentPadding = 14.dp) {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Column(modifier = Modifier.weight(1f)) {
                    VoraOverline("Current goal")
                    Text("Build strength", style = MaterialTheme.typography.titleSmall, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
                }
                VDivider()
                Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                    VoraOverline("Weekly target")
                    Text("4 / 5 Workouts", style = MaterialTheme.typography.titleSmall, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun TrendTile(label: String, value: String, delta: String?, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.background, modifier = modifier) {
        Column(Modifier.padding(14.dp)) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 4.dp)) {
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                if (delta != null) Text("  $delta", color = VoraMint, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CategoryRow(icon: String, title: String, sub: String, onClick: () -> Unit) {
    VoraCard(onClick = onClick, contentPadding = 14.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.background, modifier = Modifier.size(42.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(icon, fontSize = 18.sp) }
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
                Text(sub, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("View  ›", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** Progress / 02 — Progress Dashboard. */
@Composable
fun ProgressDashboardScreen(
    onBackClick: () -> Unit,
    onWorkoutViewClick: () -> Unit,
    onNutritionViewClick: () -> Unit,
    onActivityViewClick: () -> Unit,
    onSeeWeeklyReviewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        Column {
            VoraTitle("Your progress")
            Text("Small improvements add up.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        PeriodTabs()
        VoraCard {
            VoraOverline("This week overview")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 8.dp)) {
                TrendTile("Workouts completed", "4 workouts", "↑ 1", Modifier.weight(1f))
                TrendTile("Avg Daily Steps", "7,842", "↑ 6%", Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 10.dp)) {
                TrendTile("Daily Avg Protein", "104g", "↑ 9%", Modifier.weight(1f))
                TrendTile("Active Streak", "4 days 🔥", null, Modifier.weight(1f))
            }
        }
        VoraDarkPanel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 30.dp)
                Text("VORA Insight", color = VoraMint, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f).padding(start = 10.dp))
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f)) {
                    Text("CONSISTENT", color = VoraLime, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                }
            }
            Text(
                "\"You've completed 4 workouts this week and increased your training volume by 8%. Your consistency is trending in the right direction.\"",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                "See weekly review  →",
                color = VoraLime,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onSeeWeeklyReviewClick).padding(vertical = 4.dp),
            )
        }
        CategoryRow("🏋", "Workout", "3 sessions • 2,340 kg volume", onWorkoutViewClick)
        CategoryRow("🍎", "Nutrition", "104g protein • 74% avg target", onNutritionViewClick)
        CategoryRow("📈", "Activity", "7,842 steps • 5.6 km daily avg", onActivityViewClick)
        VoraTipCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOverline("◎ Active goal", Modifier.weight(1f), color = VoraGreen)
                Text("68% complete", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Text("Build strength & improve fitness", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
            Text("You're building consistency across workouts and nutrition.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Progress / 03 — Workout Progress. */
@Composable
fun WorkoutProgressScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    VoraScreen(modifier = modifier) {
        VoraAppBar("Workout progress", onBackClick, trailing = { MoreButton() })
        PeriodTabs()
        StatTriple(listOf("SESSIONS" to "4", "VOLUME" to "8,420 kg", "AVG TIME" to "41 min"))
        VoraCard {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Training Volume", style = MaterialTheme.typography.titleSmall, fontSize = 15.sp)
                    VoraOverline("Kilograms lifted per session")
                }
                Text("+8% vs last week", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Box(Modifier.padding(top = 16.dp)) {
                Bars(listOf(0.6f, 0.7f, 0f, 0.85f, 0.95f), listOf("M", "T", "W", "T", "F"))
            }
        }
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, border = BorderStroke(1.dp, VoraMint), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = VoraMintTint, modifier = Modifier.size(44.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("🏆", fontSize = 20.sp) }
                }
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text("PERSONAL BEST", color = Color(0xFF27AE60), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text("Seated Cable Row", style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
                    Text("45 kg × 12 reps (+5 kg vs prev best)", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF27AE60))
                }
            }
        }
        VoraTipCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 34.dp)
                Text(
                    "\"Your training volume increased without adding workout duration. That's efficiency improving.\"",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
        VoraOverline("Exercise progress")
        listOf(
            Triple("Bench Press", "40 kg × 10 reps", "↑ 2.5 kg"),
            Triple("Cable Row", "45 kg × 12 reps", "↑ 5.0 kg"),
            Triple("Shoulder Press", "20 kg × 10 reps", "→ Stable"),
            Triple("Lateral Raise", "8 kg × 12 reps", "↑ 1.0 kg"),
        ).forEach { (n, s, d) ->
            VoraCard(contentPadding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(n, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
                        Text(s, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(shape = CircleShape, color = if (d.startsWith("↑")) VoraMintTint else MaterialTheme.colorScheme.background) {
                        Text(d, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = if (d.startsWith("↑")) VoraGreen else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                    }
                }
            }
        }
    }
}

/** Progress / 04 — Nutrition Progress. */
@Composable
fun NutritionProgressScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    VoraScreen(modifier = modifier) {
        VoraAppBar("Nutrition progress", onBackClick, trailing = { MoreButton() })
        PeriodTabs()
        StatTriple(listOf("AVG CALORIES" to "1,980 kcal", "AVG PROTEIN" to "132g", "TARGET HIT" to "5 / 7 days"))
        VoraCard {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Calories", "Protein", "Carbs", "Fat").forEach { t ->
                    val sel = t == "Protein"
                    Surface(shape = RoundedCornerShape(10.dp), color = if (sel) VoraGreen else MaterialTheme.colorScheme.background, modifier = Modifier.weight(1f)) {
                        Text(
                            t,
                            color = if (sel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 9.dp),
                        )
                    }
                }
            }
            Text("DAILY AVG PROTEIN TARGET (140g)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 14.dp, bottom = 10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("M" to true, "T" to true, "W" to false, "T" to true, "F" to true, "S" to false, "S" to true).forEach { (d, hit) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(shape = CircleShape, color = if (hit) VoraMintTint else MaterialTheme.colorScheme.background, border = BorderStroke(1.dp, if (hit) VoraMint else VoraBorder), modifier = Modifier.size(38.dp)) {
                            Box(contentAlignment = Alignment.Center) { Text(d, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = if (hit) VoraGreen else VoraTextMuted) }
                        }
                        Text(if (hit) "HIT" else "MISS", fontSize = 10.sp, color = if (hit) VoraMint else VoraTextMuted, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
        VoraDarkPanel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 30.dp)
                Text("Nutrition Analysis", color = VoraMint, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 10.dp))
            }
            Text(
                "\"Your protein intake has improved this week, especially on workout days. Keeping this consistent will better support your strength goal.\"",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        VoraOverline("Food habits & patterns")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HabitTile("Frequent lunch", "Chicken & Rice Bowl", Modifier.weight(1f))
            HabitTile("Top protein source", "Whey & Chicken Breast", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HabitTile("Avg breakfast", "420 kcal", Modifier.weight(1f))
            HabitTile("Avg dinner", "610 kcal", Modifier.weight(1f))
        }
    }
}

@Composable
private fun HabitTile(label: String, value: String, modifier: Modifier = Modifier) {
    VoraCard(modifier = modifier, contentPadding = 14.dp) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleSmall, fontSize = 17.sp, modifier = Modifier.padding(top = 2.dp))
    }
}

/** Progress / 05 — Health Trends. */
@Composable
fun HealthTrendsScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    VoraScreen(modifier = modifier) {
        Row(verticalAlignment = Alignment.Top) {
            VoraAppBar("Health trends", onBackClick, modifier = Modifier.weight(1f))
            MoreButton()
        }
        Text("See how your daily habits are changing.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        StatTriple(listOf("AVG STEPS" to "7,842", "AVG DISTANCE" to "5.6 km", "ACTIVE KCAL" to "312 kcal"))
        VoraCard {
            Row {
                Text("Weekly Steps Trend", style = MaterialTheme.typography.titleSmall, fontSize = 15.sp, modifier = Modifier.weight(1f))
                Text("Goal: 8,000", color = VoraMint, fontSize = 13.sp)
            }
            Box(Modifier.padding(top = 16.dp)) {
                Bars(listOf(0.6f, 0.75f, 0.8f, 0.7f, 0.85f, 0.72f, 0.74f), listOf("M", "T", "W", "T", "F", "S", "S"), highlight = 2, light = true)
            }
        }
        VoraTipCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 34.dp)
                Text(
                    "\"Your activity has been more consistent over the last two weeks. Your workout days also tend to be your most active days.\"",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
        VoraOverline("Habit signals")
        listOf(
            Triple("🛏", "Sleep Quality", "Improving ↗"),
            Triple("🧭", "Daily Movement", "Consistent"),
            Triple("🏋", "Workout Frequency", "Improving ↗"),
            Triple("🍎", "Protein Intake", "Improving ↗"),
        ).forEach { (icon, name, state) ->
            VoraCard(contentPadding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(icon, fontSize = 18.sp)
                    Text(name, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp, modifier = Modifier.weight(1f).padding(start = 12.dp))
                    Text(state, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (state.startsWith("Improving")) VoraMint else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/** Progress / 06 — Weekly Review. */
@Composable
fun WeeklyReviewScreen(
    onBackClick: () -> Unit,
    onAskVoraClick: () -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().background(VoraGreen).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraAppBar("Your weekly review", onBackClick, dark = true, modifier = Modifier.weight(1f))
            VoraOrb(size = 36.dp)
        }
        Text("Here's what changed this week.", color = VoraMint, style = MaterialTheme.typography.bodyMedium)
        Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.08f), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                Text("\"You had a strong week.\"", color = VoraLime, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Row(modifier = Modifier.padding(top = 12.dp)) {
                    listOf("WORKOUTS" to "4 sessions", "AVG STEPS" to "7,842 steps", "PROTEIN" to "132g daily").forEach { (l, v) ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(l, color = DarkSub, fontSize = 10.sp)
                            Text(v, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 3.dp))
                        }
                    }
                }
            }
        }
        Text("WHAT WENT WELL", color = VoraMint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        listOf(
            "You stayed consistent" to "Four completed workouts helped you maintain your weekly training rhythm.",
            "Your protein improved" to "You reached your protein target on 5 of 7 days.",
            "Your training volume increased" to "You completed 8% more volume than last week.",
        ).forEach { (t, b) ->
            Row {
                Surface(shape = CircleShape, color = VoraMint.copy(alpha = 0.25f), modifier = Modifier.size(28.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("✓", color = VoraMint, fontSize = 14.sp) }
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(t, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(b, color = DarkSub, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Text("WHAT TO IMPROVE", color = VoraLime, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
        Surface(shape = RoundedCornerShape(16.dp), color = Color.White.copy(alpha = 0.08f), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("ⓘ  Dinner consistency", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("Your protein intake tends to drop at dinner. Try planning a protein-rich dinner before the evening.", color = DarkSub, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
                // "Get dinner ideas" es solo visual.
                Text("Get dinner ideas  →", color = VoraLime, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 10.dp))
            }
        }
        Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                VoraOverline("Next week's recommendations")
                listOf("Keep your current workout schedule.", "Try to reach 140g protein on at least 6 days.", "Aim for 8,000+ steps on non-training days.").forEach {
                    Text("•  $it", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
                }
                Box(Modifier.padding(vertical = 12.dp).fillMaxWidth().height(1.dp).background(VoraBorder))
                Text("View next week's plan  →", color = VoraGreen, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        VoraLimeButton("🎙  Ask VORA", onAskVoraClick)
        Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
            modifier = Modifier.fillMaxWidth().clip(CircleShape).clickable(onClick = onDoneClick),
        ) {
            Text("Done", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(vertical = 15.dp))
        }
    }
}
