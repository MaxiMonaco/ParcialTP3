package com.example.parcialtp3.ui.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.viewmodel.WorkoutsViewModel
import com.example.parcialtp3.viewmodel.WorkoutsUiState
import com.example.parcialtp3.viewmodel.WorkoutDetailViewModel
import com.example.parcialtp3.viewmodel.WorkoutDetailUiState
import com.example.parcialtp3.viewmodel.ExerciseViewModel
import com.example.parcialtp3.viewmodel.ExerciseUiState
import com.example.parcialtp3.shared.VoraWarningBlock
import com.example.parcialtp3.shared.VoraSectionTitle
import com.example.parcialtp3.shared.VoraLoadingBlock
import com.example.parcialtp3.shared.VoraErrorBlock
import com.example.parcialtp3.shared.VoraEmptyBlock
import com.example.parcialtp3.model.WorkoutDetail
import com.example.parcialtp3.model.Workout
import com.example.parcialtp3.model.Exercise
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator
import com.example.parcialtp3.shared.VoraAppBar
import com.example.parcialtp3.shared.VoraBigOrb
import com.example.parcialtp3.shared.DemoDialogHost
import com.example.parcialtp3.shared.rememberDemoDialog
import com.example.parcialtp3.shared.VoraCard
import com.example.parcialtp3.shared.VoraChip
import com.example.parcialtp3.shared.VoraDarkCard
import com.example.parcialtp3.shared.VoraDarkChip
import com.example.parcialtp3.shared.VoraDarkPanel
import com.example.parcialtp3.shared.VoraIconButton
import com.example.parcialtp3.shared.VoraLimeButton
import com.example.parcialtp3.shared.VoraOrb
import com.example.parcialtp3.shared.VoraOutlinedButton
import com.example.parcialtp3.shared.VoraOverline
import com.example.parcialtp3.shared.VoraPrimaryButton
import com.example.parcialtp3.shared.VoraScreen
import com.example.parcialtp3.shared.VoraStatusPill
import com.example.parcialtp3.shared.VoraTextLink
import com.example.parcialtp3.shared.VoraTipCard
import com.example.parcialtp3.shared.VoraTitle
import com.example.parcialtp3.ui.theme.VoraBorder
import com.example.parcialtp3.ui.theme.VoraGreen
import com.example.parcialtp3.ui.theme.VoraLime
import com.example.parcialtp3.ui.theme.VoraMint
import com.example.parcialtp3.ui.theme.VoraMintTint
import com.example.parcialtp3.ui.theme.VoraTextMuted

/*
 * Workout Flow. Catalogo, detalle, preparacion, sesion activa y guia de ejercicio leen de un
 * ViewModel (Hilt) que combina Retrofit + cache en Room (RF01, RF02, RF05, RF07). Cada pantalla
 * sigue recibiendo solo lambdas de navegacion: el ViewModel se pide con hiltViewModel() y toma el
 * id del entrenamiento o ejercicio de los argumentos de la ruta (NAV04).
 * Plan semanal, coach, cronometro y volumen siguen siendo de demostracion.
 */

private val DarkText = Color.White
private val DarkSub = Color.White.copy(alpha = 0.7f)

@Composable
private fun DarkFrame(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize().background(VoraGreen)) { content() }
}

@Composable
private fun CoachLabel(text: String, color: Color = MaterialTheme.colorScheme.onSurface) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        VoraOrb(size = 18.dp)
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 8.dp))
    }
}

/** Workout / 01 — Dashboard. Catalogo remoto (Retrofit) con cache en Room: RF01, RF02, RF05. */
@Composable
fun WorkoutDashboardScreen(
    onStartWorkoutClick: (workoutId: String) -> Unit,
    onWorkoutDetailsClick: (workoutId: String) -> Unit,
    onWeeklyPlanClick: () -> Unit,
    onViewProgressClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    VoraScreen(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Workout Goal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                VoraTitle("Workouts")
            }
            VoraIconButton("📅", onWeeklyPlanClick)
        }

        when (val current = state) {
            is WorkoutsUiState.Loading -> VoraLoadingBlock()
            is WorkoutsUiState.Error -> VoraErrorBlock(current.message, onRetry = viewModel::retry)
            is WorkoutsUiState.Empty -> VoraEmptyBlock("No hay entrenamientos disponibles por ahora.", onRetry = viewModel::retry)
            is WorkoutsUiState.Content -> {
                current.warning?.let { VoraWarningBlock(it, onRetry = viewModel::retry) }
                WorkoutCatalog(current.workouts, onStartWorkoutClick, onWorkoutDetailsClick)
            }
        }

        VoraTipCard {
            CoachLabel("VORA RECOMMENDS", VoraGreen)
            Text(
                "Your last upper-body session was 4 days ago. Today is a great opportunity to hit your strength volume targets.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        VoraCard {
            Text("This Week's Activity", style = MaterialTheme.typography.titleSmall, fontSize = 15.sp)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("M" to "done", "T" to "done", "W" to "rest", "T" to "done", "F" to "fit", "S" to "rest", "S" to "rest").forEach { (d, s) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(d, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(6.dp))
                        val bg = when (s) { "done" -> VoraMint; "fit" -> VoraGreen; else -> MaterialTheme.colorScheme.background }
                        Surface(shape = CircleShape, color = bg, border = if (s == "rest") BorderStroke(1.dp, VoraBorder) else null, modifier = Modifier.size(38.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                if (s == "rest") Text("Zzz", fontSize = 9.sp, color = VoraTextMuted)
                                if (s == "fit") Text("FIT", fontSize = 9.sp, color = VoraMint, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        VoraCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Your Progress", style = MaterialTheme.typography.titleSmall, fontSize = 15.sp, modifier = Modifier.weight(1f))
                VoraTextLink("View progress", onViewProgressClick)
            }
            Row(modifier = Modifier.padding(top = 8.dp).height(IntrinsicSize.Min)) {
                StatBlock("STREAK", "4 Days", Modifier.weight(1f))
                VDivider()
                StatBlock("WORKOUTS", "3/5 complete", Modifier.weight(1f).padding(start = 12.dp))
                VDivider()
                StatBlock("VOL. LIFTED", "8,420 kg", Modifier.weight(1f).padding(start = 12.dp))
            }
        }
    }
}

/** El primer entrenamiento del catalogo va destacado; debajo se listan todos y cada uno abre SU detalle (NAV04). */
@Composable
private fun WorkoutCatalog(
    workouts: List<Workout>,
    onStartWorkoutClick: (workoutId: String) -> Unit,
    onWorkoutDetailsClick: (workoutId: String) -> Unit,
) {
    val featured = workouts.first()
    VoraDarkPanel {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("READY FOR TODAY?", color = VoraMint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            if (featured.level.isNotBlank()) VoraStatusPill(featured.level.uppercase(), dark = true)
        }
        Text(featured.name, color = DarkText, style = MaterialTheme.typography.headlineSmall)
        Text(workoutSummary(featured), color = DarkSub, style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            VoraLimeButton("Start Workout", { onStartWorkoutClick(featured.id) }, modifier = Modifier.weight(1f))
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                modifier = Modifier.clip(CircleShape).clickable { onWorkoutDetailsClick(featured.id) },
            ) {
                Text("Details", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 22.dp, vertical = 14.dp))
            }
        }
    }
    VoraSectionTitle("All workouts")
    workouts.forEach { workout ->
        VoraCard(onClick = { onWorkoutDetailsClick(workout.id) }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(workout.name, style = MaterialTheme.typography.titleSmall, fontSize = 15.sp)
                    Text(workoutSummary(workout), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("→", color = VoraTextMuted, fontSize = 16.sp)
            }
        }
    }
}

/** "25 min • 3 exercises • Beginner": solo muestra lo que la API mando. */
private fun workoutSummary(workout: Workout): String =
    listOfNotNull(
        workout.durationMinutes?.let { "$it min" },
        workout.exerciseCount?.let { "$it exercises" },
        workout.level.takeIf { it.isNotBlank() }?.replaceFirstChar { it.uppercase() },
    ).joinToString(" • ")

@Composable
private fun StatBlock(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        VoraOverline(label)
        Text(value, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun VDivider() {
    Box(Modifier.padding(start = 12.dp).width(1.dp).fillMaxHeight().background(VoraBorder))
}

/** Workout / 02 — Weekly Plan. Demostracion; los entrenamientos de hoy y del jueves salen del catalogo. */
@Composable
fun WeeklyPlanScreen(
    onWorkoutClick: (workoutId: String) -> Unit,
    onAskVoraClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val workouts = (state as? WorkoutsUiState.Content)?.workouts.orEmpty()
    val today = workouts.getOrNull(0)
    val thursday = workouts.getOrNull(1)
    VoraScreen(modifier = modifier) {
        Column {
            VoraTitle("Your Plan")
            Text("Designed around your goals and recovery.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("MON" to "7", "TUE" to "8", "WED" to "9", "THU" to "10", "FRI" to "11", "SAT" to "12", "SUN" to "13").forEach { (d, n) ->
                val selected = d == "TUE"
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (selected) VoraGreen else Color.Transparent,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 9.dp, vertical = 10.dp)) {
                        Text(d, fontSize = 10.sp, color = if (selected) VoraMint else MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(n, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 6.dp))
                    }
                }
            }
        }
        // Entrenamiento de hoy (borde verde): el primero del catalogo
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.5.dp, VoraGreen),
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable { today?.let { onWorkoutClick(it.id) } },
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("TODAY'S WORKOUT", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    Surface(shape = RoundedCornerShape(6.dp), color = VoraMint) {
                        Text("READY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VoraGreen, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                    }
                }
                Text(today?.name ?: "Sin entrenamiento cargado", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                Text(
                    today?.let { workoutSummary(it) } ?: "Abrí la pestaña Workout con conexión para descargar el catálogo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        VoraCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    VoraOverline("Wednesday")
                    Text("Active Recovery", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp))
                    Text("25 min • Mobility + Walk", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                VoraOrb(size = 26.dp)
            }
        }
        VoraCard(onClick = { thursday?.let { onWorkoutClick(it.id) } }) {
            VoraOverline("Thursday")
            Text(thursday?.name ?: "Rest day", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp))
            Text(
                thursday?.let { workoutSummary(it) } ?: "No workout scheduled",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        VoraDarkPanel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 20.dp)
                Text("NEED TO ADJUST YOUR WEEK?", color = VoraMint, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 8.dp))
            }
            Text(
                "Tell VORA what changed (e.g., sleep, muscle soreness, or schedule cuts) and your coach will realign the week dynamically.",
                color = DarkSub,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VoraDarkChip("I'm tired today", onAskVoraClick)
                VoraDarkChip("Move Wed workout to Fri", onAskVoraClick)
            }
        }
    }
}

/** Workout / 03 — Workout Details. GET /v1/workouts/{id} con cache en Room (RF01, RF02, RF05). */
@Composable
fun WorkoutDetailsScreen(
    @Suppress("UNUSED_PARAMETER") workoutId: String, // el ViewModel lo toma solo de los argumentos de la ruta
    onBackClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onExerciseClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailViewModel = hiltViewModel(),
) {
    val demo = rememberDemoDialog()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    VoraScreen(modifier = modifier) {
        DemoDialogHost(demo)
        VoraAppBar("Workout Overview", onBackClick)
        when (val current = state) {
            is WorkoutDetailUiState.Loading -> VoraLoadingBlock()
            is WorkoutDetailUiState.Error -> VoraErrorBlock(current.message, onRetry = viewModel::retry)
            is WorkoutDetailUiState.Content -> {
                val workout = current.workout
                current.warning?.let { VoraWarningBlock(it, onRetry = viewModel::retry) }
                Column {
                    VoraTitle(workout.name)
                    Text(
                        listOfNotNull(
                            workout.durationMinutes?.let { "$it min" },
                            workout.level.takeIf { it.isNotBlank() }?.replaceFirstChar { it.uppercase() },
                            "${workout.exercises.size} exercises",
                        ).joinToString("  •  "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
                VoraTipCard {
                    VoraOverline("VORA Coach note", color = VoraGreen)
                    Text(
                        "Based on your last session, VORA suggests keeping the same weights and focusing on controlled repetitions today.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
                if (workout.exercises.isEmpty()) {
                    VoraEmptyBlock("Este entrenamiento no tiene ejercicios cargados.")
                }
                workout.exercises.forEach { exercise ->
                    VoraCard(onClick = { onExerciseClick(exercise.id) }, contentPadding = 12.dp) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.background, modifier = Modifier.size(40.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("%02d".format(exercise.position + 1), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = VoraGreen)
                                }
                            }
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(exercise.name, style = MaterialTheme.typography.titleSmall, fontSize = 15.sp)
                                Text(exerciseSummary(exercise), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                VoraPrimaryButton("Start Workout", onStartWorkoutClick)
                VoraOutlinedButton("Edit workout", onClick = { demo.show("Editar entrenamientos no está incluido en esta versión.") })
            }
        }
    }
}

/** "3 × 10 • Rest 90s" */
private fun exerciseSummary(exercise: Exercise): String =
    listOfNotNull(
        "${exercise.sets} × ${exercise.reps}",
        exercise.restSeconds?.let { "Rest ${it}s" },
    ).joinToString(" • ")

/** Workout / 04 — Pre Workout. */
@Composable
fun PreWorkoutScreen(
    @Suppress("UNUSED_PARAMETER") workoutId: String, // el ViewModel lo toma solo de los argumentos de la ruta
    onCloseClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onAskVoraClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DarkFrame(modifier) {
        when (val current = state) {
            is WorkoutDetailUiState.Loading -> DarkStatus(message = null, onRetry = null, onClose = onCloseClick)
            is WorkoutDetailUiState.Error -> DarkStatus(message = current.message, onRetry = viewModel::retry, onClose = onCloseClick)
            is WorkoutDetailUiState.Content -> PreWorkoutContent(current.workout, onCloseClick, onStartWorkoutClick, onAskVoraClick)
        }
    }
}

@Composable
private fun PreWorkoutContent(
    workout: WorkoutDetail,
    onCloseClick: () -> Unit,
    onStartWorkoutClick: () -> Unit,
    onAskVoraClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("GET READY", color = VoraMint, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text("✕", color = Color.White, fontSize = 16.sp, modifier = Modifier.clickable(onClick = onCloseClick).padding(8.dp))
        }
        Column {
            Text(workout.name, color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text(
                listOfNotNull(workout.durationMinutes?.let { "$it min" }, "${workout.exercises.size} exercises").joinToString(" • "),
                color = VoraMint,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        PreCard("TODAY'S FOCUS", "Controlled Repetitions", "Keep concentric lifts explosive and focus on a slow, 2-second eccentric return for maximal hypertrophy stimulus.")
        PreCard("YOUR LAST SESSION", "Bench Press Volume", "Set 1: 40 kg × 10 • Set 2: 40 kg × 9 • Set 3: 37.5 kg × 10")
        VoraDarkCard {
            Text("VORA'S COACH TIP", color = VoraLime, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "\"Start slightly lighter on your first set. If your form feels exceptionally robust, increase the load.\"",
                color = VoraMint,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White.copy(alpha = 0.1f),
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable(onClick = onAskVoraClick),
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 28.dp)
                Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text("Need anything?", color = VoraMint, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text("\"Recommend a chest warm-up\"", color = DarkSub, fontSize = 12.sp)
                }
                Surface(shape = CircleShape, color = VoraLime, modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("🎙", fontSize = 16.sp) }
                }
            }
        }
        Surface(
            shape = CircleShape,
            color = VoraMint,
            modifier = Modifier.fillMaxWidth().clip(CircleShape).clickable(onClick = onStartWorkoutClick),
        ) {
            Text("Start Workout", color = VoraGreen, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 16.dp))
        }
    }
}

/** Carga (message == null) o error sobre fondo oscuro. `onClose` es null donde no hay a donde cerrar. */
@Composable
private fun DarkStatus(message: String?, onRetry: (() -> Unit)?, onClose: (() -> Unit)?) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (message == null) {
            CircularProgressIndicator(color = VoraMint)
        } else {
            Text(message, color = Color.White, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            if (onRetry != null) VoraLimeButton("Reintentar", onRetry)
        }
        if (onClose != null) {
            Spacer(Modifier.height(12.dp))
            VoraTextLink("Cerrar", onClose, muted = true)
        }
    }
}

@Composable
private fun PreCard(label: String, title: String, body: String) {
    VoraDarkCard {
        Text(label, color = VoraLime, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium, fontSize = 17.sp, modifier = Modifier.padding(top = 6.dp))
        Text(body, color = DarkSub, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
    }
}

/** Workout / 05 — Active Workout. Ejercicios reales; cronometro, descanso y volumen simulados. */
@Composable
fun ActiveWorkoutScreen(
    @Suppress("UNUSED_PARAMETER") workoutId: String, // el ViewModel lo toma solo de los argumentos de la ruta
    onFinishWorkoutClick: () -> Unit,
    onExerciseGuideClick: (exerciseId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DarkFrame(modifier) {
        when (val current = state) {
            is WorkoutDetailUiState.Loading -> DarkStatus(message = null, onRetry = null, onClose = null)
            is WorkoutDetailUiState.Error -> DarkStatus(message = current.message, onRetry = viewModel::retry, onClose = null)
            is WorkoutDetailUiState.Content -> ActiveWorkoutContent(current.workout, onFinishWorkoutClick, onExerciseGuideClick)
        }
    }
}

@Composable
private fun ActiveWorkoutContent(
    workout: WorkoutDetail,
    onFinishWorkoutClick: () -> Unit,
    onExerciseGuideClick: (exerciseId: String) -> Unit,
) {
    // Ejercicio y serie actuales: estado local simulado (no se guarda). "Complete Set" avanza la
    // serie, al terminar las series pasa al ejercicio siguiente y en el ultimo termina el entrenamiento.
    var exerciseIndex by remember { mutableIntStateOf(0) }
    var currentSet by remember { mutableIntStateOf(1) }
    val exercises = workout.exercises
    val exercise = exercises.getOrNull(exerciseIndex)
    val totalSets = exercise?.sets ?: 1
    val isLastSet = currentSet >= totalSets
    val isLastExercise = exerciseIndex >= exercises.lastIndex
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f).clickable { exercise?.let { onExerciseGuideClick(it.id) } }) {
                Text("EXERCISE ${exerciseIndex + 1} OF ${exercises.size}", color = VoraMint, fontSize = 12.sp)
                Text(exercise?.name ?: "No exercises", color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
            Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f), modifier = Modifier.clip(CircleShape).clickable(onClick = onFinishWorkoutClick)) {
                Text("Finish Workout", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
            }
        }
        VoraDarkCard {
            Text("CURRENT VOLUME WORK", color = VoraMint, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(exercise?.reps ?: "-", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.SemiBold)
                    Text("REPS TARGET", color = DarkSub, fontSize = 10.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$totalSets × ${exercise?.reps ?: "-"}", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.SemiBold)
                    Text("SET MATRIX", color = DarkSub, fontSize = 10.sp)
                }
            }
        }
        Surface(shape = RoundedCornerShape(16.dp), color = Color.White.copy(alpha = 0.08f), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("LAST SESSION", color = DarkSub, fontSize = 11.sp)
                    Text("45 kg × 12 reps", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                RoundGlyph("–")
                Text("45 kg", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 12.dp))
                RoundGlyph("+")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (number in 1..totalSets) {
                SetPill("$number", "Set $number", done = currentSet > number, selected = currentSet == number, modifier = Modifier.weight(1f))
            }
        }
        Surface(shape = RoundedCornerShape(14.dp), color = Color.White.copy(alpha = 0.08f), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Resting • 01:18", color = VoraMint, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Surface(shape = CircleShape, color = Color.Transparent, border = BorderStroke(1.dp, VoraMint)) {
                    Text("+30s", color = VoraMint, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
                Surface(shape = CircleShape, color = VoraMint, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Skip", color = VoraGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                }
            }
        }
        VoraDarkCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 18.dp)
                Text("VORA COACH", color = VoraMint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f).padding(start = 8.dp))
                Text("Voice On", color = DarkSub, fontSize = 11.sp)
            }
            Text(
                "\"Two more reps. Keep your chest elevated, brace your core, and control the bar's return.\"",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        VoraLimeButton(
            if (isLastSet && isLastExercise) "Finish Workout" else if (isLastSet) "Next Exercise" else "Complete Set",
            onClick = {
                if (!isLastSet) {
                    currentSet++
                } else if (!isLastExercise) {
                    exerciseIndex++
                    currentSet = 1
                } else {
                    onFinishWorkoutClick()
                }
            },
        )
    }
}

@Composable
private fun RoundGlyph(glyph: String) {
    Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f), modifier = Modifier.size(34.dp)) {
        Box(contentAlignment = Alignment.Center) { Text(glyph, color = Color.White, fontSize = 18.sp) }
    }
}

@Composable
private fun SetPill(n: String, label: String, modifier: Modifier = Modifier, done: Boolean = false, selected: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.08f),
        border = if (selected) BorderStroke(1.5.dp, VoraLime) else BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        modifier = modifier,
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = if (done) VoraMint else if (selected) VoraLime else Color.White.copy(alpha = 0.15f), modifier = Modifier.size(22.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(n, color = VoraGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            }
            Text(label, color = Color.White, fontSize = 13.sp, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

/** Workout / 06 — Exercise Details. Sale de la cache de Room (se guardo al descargar el detalle del entrenamiento). */
@Composable
fun ExerciseDetailsScreen(
    @Suppress("UNUSED_PARAMETER") exerciseId: String, // el ViewModel lo toma solo de los argumentos de la ruta
    onBackClick: () -> Unit,
    onBackToWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseViewModel = hiltViewModel(),
) {
    val demo = rememberDemoDialog()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    VoraScreen(modifier = modifier) {
        DemoDialogHost(demo)
        VoraAppBar("Exercise Guide", onBackClick, trailing = { VoraIconButton("?", onClick = { demo.show("Guía de ejercicio de demostración: técnica y músculos objetivo.") }, size = 36.dp) })
        when (val current = state) {
            is ExerciseUiState.Loading -> VoraLoadingBlock()
            is ExerciseUiState.NotFound -> {
                VoraEmptyBlock("Esta guía no está disponible. Abrí el detalle del entrenamiento con conexión para guardarla.")
                VoraPrimaryButton("Back to Workout", onBackToWorkoutClick)
            }
            is ExerciseUiState.Content -> {
                val exercise = current.exercise
                Column {
                    VoraTitle(exercise.name)
                    Text("${exercise.sets} sets × ${exercise.reps} reps", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                }
                // Foto del ejercicio (en Figma es una imagen): bloque provisorio.
                Box(
                    modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFF2E3A37)),
                    contentAlignment = Alignment.Center,
                ) { Text("🏋", fontSize = 48.sp) }
                Text("Performance Steps", style = MaterialTheme.typography.titleSmall, fontSize = 15.sp)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (exercise.instructions.isEmpty()) {
                        Text("Este ejercicio no trae instrucciones.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    exercise.instructions.forEachIndexed { index, step ->
                        Text("${index + 1}. $step", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                exercise.restSeconds?.let { rest ->
                    VoraCard {
                        VoraOverline("Rest between sets", color = VoraGreen)
                        Text("$rest seconds", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
                    }
                }
                VoraTipCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VoraOrb(size = 22.dp)
                        Text("Ask Vora: \"Explain grip variations\"", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 10.dp))
                    }
                }
                VoraPrimaryButton("Back to Workout", onBackToWorkoutClick)
            }
        }
    }
}

/** Workout / 07 — Workout Complete. */
@Composable
fun WorkoutCompleteScreen(
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        Text("COMPLETED", color = VoraMint, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { VoraBigOrb(size = 180.dp) }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("You did well!", style = MaterialTheme.typography.headlineMedium)
            Text("Strong session. Your consistency is improving.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp))
        }
        VoraCard {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                CompleteStat("42 min", "DURATION")
                CompleteStat("5/5", "EXERCISES")
                CompleteStat("2,340 kg", "VOLUME")
            }
        }
        VoraTipCard {
            CoachLabel("VORA INSIGHT", VoraGreen)
            Text(
                "You increased your Seated Cable Row volume by 8% compared with your previous session. Keep this controlled tempo next time.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        Text("Rate this workout difficulty", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
            VoraChip("Great")
            Surface(shape = CircleShape, color = VoraGreen) {
                Text("Good", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp))
            }
            VoraChip("Tough")
            VoraChip("Too Easy")
        }
        VoraPrimaryButton("Done", onDoneClick)
    }
}

@Composable
private fun CompleteStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, fontSize = 10.sp, color = VoraTextMuted, modifier = Modifier.padding(top = 2.dp))
    }
}
