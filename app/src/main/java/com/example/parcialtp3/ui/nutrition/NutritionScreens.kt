package com.example.parcialtp3.ui.nutrition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.navigation.SampleIds
import com.example.parcialtp3.shared.VoraAppBar
import com.example.parcialtp3.shared.VoraBigOrb
import com.example.parcialtp3.shared.DemoDialogHost
import com.example.parcialtp3.shared.rememberDemoDialog
import com.example.parcialtp3.shared.VoraCard
import com.example.parcialtp3.shared.VoraDarkCard
import com.example.parcialtp3.shared.VoraDarkPanel
import com.example.parcialtp3.shared.VoraIconButton
import com.example.parcialtp3.shared.VoraLimeButton
import com.example.parcialtp3.shared.VoraOrb
import com.example.parcialtp3.shared.VoraOutlinedButton
import com.example.parcialtp3.shared.VoraOverline
import com.example.parcialtp3.shared.VoraPrimaryButton
import com.example.parcialtp3.shared.VoraProgressBar
import com.example.parcialtp3.shared.VoraRing
import com.example.parcialtp3.shared.VoraScreen
import com.example.parcialtp3.shared.VoraSectionTitle
import com.example.parcialtp3.shared.VoraStatusPill
import com.example.parcialtp3.shared.VoraTextField
import com.example.parcialtp3.shared.VoraTextLink
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
 * Nutrition Flow (solo visual, datos de ejemplo de Figma). Sin base de datos ni IA:
 * los totales, comidas y porciones son texto fijo.
 */

private val DarkSub = Color.White.copy(alpha = 0.7f)

@Composable
private fun Macro(label: String, value: String, goal: String, progress: Float, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(vertical = 4.dp)) {
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(goal, fontSize = 11.sp, color = VoraTextMuted)
        }
        VoraProgressBar(progress, color = color, height = 4.dp)
    }
}

@Composable
private fun MealRow(name: String, description: String, kcal: String, onClick: () -> Unit) {
    VoraCard(onClick = onClick, contentPadding = 16.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleSmall, fontSize = 15.sp)
                Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(kcal, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** Nutrition / 01 — Dashboard. */
@Composable
fun NutritionDashboardScreen(
    onTodaysMealsClick: () -> Unit,
    onMealClick: (mealId: String) -> Unit,
    onAddDinnerClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onPhotoClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                VoraOverline("Tuesday, 8 September")
                VoraTitle("Nutrition")
            }
            VoraIconButton("📅", onTodaysMealsClick)
        }
        VoraCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    VoraOverline("Calories consumed")
                    Text("1,420", fontSize = 34.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
                    Text("Goal: 2,100 kcal", style = MaterialTheme.typography.bodyMedium, color = VoraTextMuted)
                }
                VoraRing(0.67f, "67%", size = 84.dp)
            }
            Box(Modifier.padding(vertical = 12.dp).fillMaxWidth().height(1.dp).background(VoraBorder))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Macro("Protein", "104g", "/140g", 104f / 140f, VoraGreen, Modifier.weight(1f))
                Macro("Carbs", "152g", "/220g", 152f / 220f, VoraMint, Modifier.weight(1f))
                Macro("Fat", "48g", "/65g", 48f / 65f, VoraLime, Modifier.weight(1f))
            }
        }
        VoraTipCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 16.sp)
                Text(
                    "You're on track today. A protein-rich dinner would help you reach your target.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            VoraSectionTitle("Today's Meals", Modifier.weight(1f))
            VoraTextLink("View History", onTodaysMealsClick)
        }
        MealRow("Breakfast", "Greek yoghurt · berries · granola", "420 kcal") { onMealClick(SampleIds.MEAL_BREAKFAST) }
        MealRow("Lunch", "Chicken · rice · vegetables", "520 kcal") { onMealClick(SampleIds.MEAL_LUNCH) }
        MealRow("Snack", "Protein shake", "240 kcal") { onMealClick(SampleIds.MEAL_SNACK) }
        VoraCard(contentPadding = 16.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Dinner", style = MaterialTheme.typography.titleSmall, fontSize = 15.sp)
                    Text("Not logged yet", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(shape = CircleShape, color = VoraGreen, modifier = Modifier.clip(CircleShape).clickable(onClick = onAddDinnerClick)) {
                    Text("+ Add", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp))
                }
            }
        }
        VoraDarkPanel {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VoraOrb(size = 28.dp)
                Text("Tell Vora what you ate", color = VoraMint, fontSize = 17.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 10.dp))
            }
            Text("Vora will automatically estimate the portions, ingredients, and log the nutrition details.", color = DarkSub, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionPill("🎙  Voice", onVoiceClick, lime = true, modifier = Modifier.weight(1f))
                ActionPill("📷  Photo", onPhotoClick, modifier = Modifier.weight(1f))
                ActionPill("🔍  Search", onSearchClick, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ActionPill(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, lime: Boolean = false) {
    Surface(
        shape = CircleShape,
        color = if (lime) VoraLime else Color.White.copy(alpha = 0.12f),
        modifier = modifier.clip(CircleShape).clickable(onClick = onClick),
    ) {
        Text(
            text,
            color = if (lime) VoraGreen else Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
        )
    }
}

@Composable
private fun TodaysMealCard(title: String, kcal: String, items: List<Triple<String, String, String>>, onClick: () -> Unit) {
    VoraCard(onClick = onClick) {
        Row {
            Text(title, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text(kcal, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = VoraGreen)
        }
        Box(Modifier.padding(vertical = 8.dp).fillMaxWidth().height(1.dp).background(VoraBorder))
        items.forEach { (name, grams, k) ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(grams, style = MaterialTheme.typography.bodySmall, color = VoraTextMuted)
                }
                Text(k, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** Nutrition / 02 — Today's Meals. */
@Composable
fun TodaysMealsScreen(
    onMealClick: (mealId: String) -> Unit,
    onAddDinnerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                VoraOverline("Timeline")
                VoraTitle("Today's Meals")
            }
            VoraStatusPill("1,420 kcal")
        }
        TodaysMealCard("Breakfast", "420 kcal", listOf(
            Triple("Greek Yoghurt (0% Fat)", "200g", "150 kcal"),
            Triple("Fresh Blueberries", "75g", "60 kcal"),
            Triple("Organic Honey Granola", "50g", "210 kcal"),
        )) { onMealClick(SampleIds.MEAL_BREAKFAST) }
        TodaysMealCard("Lunch", "520 kcal", listOf(
            Triple("Grilled Chicken Breast", "180g", "280 kcal"),
            Triple("Basmati Rice", "150g", "190 kcal"),
            Triple("Mixed Steamed Vegetables", "100g", "50 kcal"),
        )) { onMealClick(SampleIds.MEAL_LUNCH) }
        TodaysMealCard("Snack", "240 kcal", listOf(
            Triple("Whey Protein Shake", "350ml (water)", "240 kcal"),
        )) { onMealClick(SampleIds.MEAL_SNACK) }
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, VoraBorder),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
                Text("Dinner is not logged yet", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Surface(shape = CircleShape, color = VoraGreen, modifier = Modifier.padding(top = 12.dp).clip(CircleShape).clickable(onClick = onAddDinnerClick)) {
                    Text("+ Add Dinner", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp))
                }
            }
        }
    }
}

@Composable
private fun MethodCard(icon: String, title: String, body: String, onClick: () -> Unit) {
    VoraCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = VoraMintTint, modifier = Modifier.size(46.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(icon, fontSize = 20.sp) }
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
                Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("→", color = VoraTextMuted, fontSize = 16.sp)
        }
    }
}

/** Nutrition / 03 — Add Food. */
@Composable
fun AddFoodScreen(
    onBackClick: () -> Unit,
    onStartVoiceLoggingClick: () -> Unit,
    onSnapPhotoClick: () -> Unit,
    onLogManuallyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val demo = rememberDemoDialog()
    VoraScreen(modifier = modifier) {
        DemoDialogHost(demo)
        VoraAppBar("Add Food", onBackClick)
        VoraDarkPanel {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Talk to VORA", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Text("\"I had grilled chicken breast with basmati rice.\"", color = DarkSub, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 6.dp))
                }
                VoraOrb(size = 44.dp)
            }
            VoraLimeButton("🎙  Start Voice Logging", onStartVoiceLoggingClick)
        }
        MethodCard("📷", "Snap a Photo", "Vora estimates sizes and nutrition info.", onSnapPhotoClick)
        // "Search Database" no tiene destino en Figma: abre un aviso de demostración.
        MethodCard("🔍", "Search Database", "Search millions of local & global food items.", onClick = { demo.show("La búsqueda en base de datos de alimentos no está incluida en esta versión.") })
        MethodCard("✏", "Log Manually", "Input weight, calories and macros yourself.", onLogManuallyClick)
    }
}

/** Nutrition / 03b — Manual Food (pantalla extra: Figma no tiene frame para "Log Manually"). */
@Composable
fun ManualFoodScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        VoraAppBar("Log Manually", onBackClick)
        VoraTextField("Food name", "e.g. Grilled Chicken Breast", labelUppercase = true)
        VoraTextField("Weight (g)", "180", labelUppercase = true)
        VoraTextField("Calories (kcal)", "280", labelUppercase = true)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            VoraTextField("Protein", "38", labelUppercase = true, modifier = Modifier.weight(1f))
            VoraTextField("Carbs", "54", labelUppercase = true, modifier = Modifier.weight(1f))
            VoraTextField("Fat", "14", labelUppercase = true, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        VoraPrimaryButton("Save", onSaveClick)
    }
}

/** Nutrition / 04 — Voice Food Logging. */
@Composable
fun VoiceFoodLoggingScreen(
    onCloseClick: () -> Unit,
    onDoneSpeakingClick: () -> Unit,
    onTypeInsteadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().background(VoraGreen).padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("●  Voice Logging", color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1f))
            CloseButton(onCloseClick)
        }
        Spacer(Modifier.height(36.dp))
        VoraBigOrb(size = 230.dp)
        Spacer(Modifier.height(20.dp))
        Text("Listening...", color = VoraMint, fontSize = 26.sp, fontWeight = FontWeight.Medium)
        Text("\"Keep talking. I'm listening.\"", color = DarkSub, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 6.dp))
        Spacer(Modifier.height(32.dp))
        VoraDarkCard {
            Text("\"I had grilled chicken with rice and some mixed vegetables for lunch.\"", color = Color.White, fontSize = 17.sp, lineHeight = 25.sp)
        }
        Spacer(Modifier.height(16.dp))
        Surface(shape = CircleShape, color = VoraMint, modifier = Modifier.fillMaxWidth().clip(CircleShape).clickable(onClick = onDoneSpeakingClick)) {
            Text("Done Speaking", color = VoraGreen, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 16.dp))
        }
        VoraTextLink("Type instead", onTypeInsteadClick, modifier = Modifier.padding(top = 12.dp), muted = true)
    }
}

@Composable
private fun CloseButton(onClick: () -> Unit) {
    Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f), modifier = Modifier.size(40.dp).clip(CircleShape).clickable(onClick = onClick)) {
        Box(contentAlignment = Alignment.Center) { Text("✕", color = Color.White, fontSize = 15.sp) }
    }
}

/** Nutrition / 05 — Food Analysis. */
@Composable
fun FoodAnalysisScreen(
    onCloseClick: () -> Unit,
    onAnalysisFinishedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().background(VoraGreen).padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("VORA AI", color = DarkSub, fontSize = 15.sp, modifier = Modifier.weight(1f))
            CloseButton(onCloseClick)
        }
        Spacer(Modifier.height(40.dp))
        // Tocar el orbe simula que el analisis termino.
        VoraBigOrb(size = 200.dp, modifier = Modifier.clip(CircleShape).clickable(onClick = onAnalysisFinishedClick))
        Spacer(Modifier.height(16.dp))
        Text("Analyzing Meal...", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(32.dp))
        VoraDarkCard {
            Text("CAPTURED INPUT", color = VoraMint, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text("\"Grilled chicken, rice and mixed vegetables\"", color = Color.White, fontSize = 15.sp, modifier = Modifier.padding(top = 4.dp))
        }
        Spacer(Modifier.height(20.dp))
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            StepRow("Identifying foods...", VoraMint, Color.White)
            StepRow("Estimating portions...", VoraLime, Color.White, bold = true)
            StepRow("Calculating nutrition...", Color.White.copy(alpha = 0.18f), Color.White.copy(alpha = 0.4f))
        }
        Spacer(Modifier.height(20.dp))
        VoraTextLink("Cancel analysis", onCloseClick, muted = true)
    }
}

@Composable
private fun StepRow(text: String, dot: Color, textColor: Color, bold: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(24.dp).clip(CircleShape).background(dot))
        Text(text, color = textColor, fontSize = 17.sp, fontWeight = if (bold) FontWeight.Medium else FontWeight.Normal, modifier = Modifier.padding(start = 14.dp))
    }
}

/** Nutrition / 06 — Photo Analysis. */
@Composable
fun PhotoAnalysisScreen(
    onBackClick: () -> Unit,
    onReviewAndAddClick: () -> Unit,
    onRetakePhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    VoraScreen(modifier = modifier) {
        VoraAppBar("Analyse Meal", onBackClick)
        // Foto del plato (en Figma es una imagen): bloque provisorio con las etiquetas.
        Box(
            modifier = Modifier.fillMaxWidth().height(230.dp).clip(RoundedCornerShape(24.dp)).background(Color(0xFFBFC3C0)),
            contentAlignment = Alignment.Center,
        ) {
            Text("🍽", fontSize = 64.sp)
            Surface(shape = RoundedCornerShape(8.dp), color = VoraGreen, modifier = Modifier.align(Alignment.TopStart).padding(start = 36.dp, top = 56.dp)) {
                Text("Grilled Chicken (~180g)", color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
            }
            Surface(shape = RoundedCornerShape(8.dp), color = VoraGreen, modifier = Modifier.align(Alignment.BottomEnd).padding(end = 28.dp, bottom = 52.dp)) {
                Text("Basmati Rice (~150g)", color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
            }
        }
        VoraCard {
            Row {
                VoraOverline("VORA Estimate", Modifier.weight(1f))
                Text("Portions are estimated", style = MaterialTheme.typography.bodySmall, color = VoraTextMuted)
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Text("520 kcal", fontSize = 28.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                MacroTag("P: 38g", MaterialTheme.colorScheme.background)
                MacroTag("C: 54g", VoraMintTint)
                MacroTag("F: 14g", Color(0xFFF1F7E1))
            }
        }
        Text(
            "\"Portions and quantities can be refined in the next step before saving.\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        VoraPrimaryButton("Review & Add", onReviewAndAddClick)
        VoraOutlinedButton("Retake Photo", onRetakePhotoClick)
    }
}

@Composable
private fun MacroTag(text: String, bg: Color) {
    Surface(shape = RoundedCornerShape(6.dp), color = bg, modifier = Modifier.padding(start = 6.dp)) {
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
    }
}

@Composable
private fun TotalsBlock(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        listOf("PROTEIN" to "38g", "CARBS" to "54g", "FAT" to "14g").forEachIndexed { i, (l, v) ->
            Column(modifier = Modifier.weight(1f), horizontalAlignment = if (i == 0) Alignment.Start else if (i == 1) Alignment.CenterHorizontally else Alignment.End) {
                VoraOverline(l, color = VoraMint)
                Text(v, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

/** Nutrition / 07 — Food Confirmation. */
@Composable
fun FoodConfirmationScreen(
    onBackClick: () -> Unit,
    onAddToMealClick: () -> Unit,
    onSaveWithoutLoggingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val demo = rememberDemoDialog()
    VoraScreen(modifier = modifier) {
        DemoDialogHost(demo)
        VoraAppBar("Review Meal", onBackClick)
        listOf(
            Triple("Grilled Chicken Breast", "180g", "280 kcal"),
            Triple("Basmati Rice", "150g", "190 kcal"),
            Triple("Mixed Steamed Vegetables", "100g", "50 kcal"),
        ).forEach { (name, grams, kcal) ->
            VoraCard(contentPadding = 14.dp) {
                Row {
                    Text(name, style = MaterialTheme.typography.titleSmall, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Text(kcal, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    // Los botones -/+ y "Remove" son solo visuales (editar porciones es logica de otra etapa).
                    SmallSquare("-")
                    Text(grams, fontSize = 15.sp, modifier = Modifier.padding(horizontal = 12.dp))
                    SmallSquare("+")
                    Spacer(Modifier.weight(1f))
                    Text("Remove", color = VoraError, fontSize = 13.sp)
                }
            }
        }
        VoraTextLink("+ Add Ingredient", onClick = { demo.show("Agregar ingredientes es parte de la carga manual (Log Manually).") })
        Surface(shape = RoundedCornerShape(20.dp), color = VoraGreen, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Row {
                    VoraOverline("Meal totals", Modifier.weight(1f), color = VoraMint)
                    Text("520 kcal", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }
                Box(Modifier.padding(vertical = 12.dp).fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.15f)))
                TotalsBlock()
            }
        }
        VoraPrimaryButton("Add to Lunch", onAddToMealClick)
        VoraOutlinedButton("Save without logging", onSaveWithoutLoggingClick)
    }
}

@Composable
private fun SmallSquare(glyph: String) {
    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.background, modifier = Modifier.size(width = 36.dp, height = 36.dp)) {
        Box(contentAlignment = Alignment.Center) { Text(glyph, fontSize = 16.sp) }
    }
}

/** Nutrition / 08 — Meal Details. */
@Composable
fun MealDetailsScreen(
    mealId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val meal = demoMeal(mealId)
    val title = meal.title
    val demo = rememberDemoDialog()
    VoraScreen(modifier = modifier) {
        DemoDialogHost(demo)
        VoraAppBar(title, onBackClick, trailing = { Text(meal.time, color = VoraTextMuted, fontSize = 13.sp) })
        VoraCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    VoraOverline("Total calories")
                    Text("${meal.kcal} kcal", fontSize = 30.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
                }
                VoraRing(meal.ring, "", size = 56.dp)
            }
            Box(Modifier.padding(vertical = 12.dp).fillMaxWidth().height(1.dp).background(VoraBorder))
            Row {
                listOf("PROTEIN" to meal.protein, "CARBS" to meal.carbs, "FAT" to meal.fat).forEachIndexed { i, (l, v) ->
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = if (i == 0) Alignment.Start else if (i == 1) Alignment.CenterHorizontally else Alignment.End) {
                        VoraOverline(l)
                        Text(v, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
        VoraTipCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 16.sp)
                Text("Great protein choice. You're now 36g away from your daily protein target.", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 10.dp))
            }
        }
        VoraSectionTitle("Ingredients")
        meal.items.forEach { (name, grams, kcal) ->
            VoraCard(contentPadding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(name, style = MaterialTheme.typography.bodyLarge)
                        Text(grams, style = MaterialTheme.typography.bodySmall, color = VoraTextMuted)
                    }
                    Text(kcal, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        // Editar / duplicar son solo visuales.
        VoraOutlinedButton("Edit Meal", onClick = { demo.show("Editar comidas no está incluido: solo se pueden cargar y eliminar.") })
        VoraOutlinedButton("Duplicate Meal", onClick = { demo.show("Duplicar comidas no está incluido en esta versión.") })
    }
}


/** Datos de ejemplo por comida (NAV04): el id de la ruta decide que detalle se muestra. */
private data class DemoMeal(
    val title: String,
    val time: String,
    val kcal: Int,
    val ring: Float,
    val protein: String,
    val carbs: String,
    val fat: String,
    val items: List<Triple<String, String, String>>,
)

private fun demoMeal(mealId: String): DemoMeal = when (mealId) {
    SampleIds.MEAL_BREAKFAST -> DemoMeal(
        "Breakfast", "08:05", 410, 0.55f, "24g", "46g", "14g",
        listOf(
            Triple("Greek Yogurt", "200g", "130 kcal"),
            Triple("Rolled Oats", "50g", "190 kcal"),
            Triple("Blueberries", "80g", "45 kcal"),
            Triple("Honey", "15g", "45 kcal"),
        ),
    )
    SampleIds.MEAL_SNACK -> DemoMeal(
        "Snack", "16:30", 180, 0.25f, "6g", "20g", "9g",
        listOf(
            Triple("Almonds", "20g", "120 kcal"),
            Triple("Apple", "150g", "60 kcal"),
        ),
    )
    else -> DemoMeal(
        "Lunch", "13:12", 520, 0.75f, "38g", "54g", "14g",
        listOf(
            Triple("Grilled Chicken Breast", "180g", "280 kcal"),
            Triple("Basmati Rice", "150g", "190 kcal"),
            Triple("Mixed Steamed Vegetables", "100g", "50 kcal"),
        ),
    )
}
