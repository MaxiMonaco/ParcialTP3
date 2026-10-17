package com.example.parcialtp3.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

/** Las 5 pestanas de Figma. El "icon" es un emoji provisorio hasta tener los iconos reales. */
enum class BottomTab(val flow: AppDestination, val label: String, val icon: String) {
    Home(AppDestination.HomeFlow, "Home", "🏠"),
    Workout(AppDestination.WorkoutFlow, "Workout", "🏋"),
    Nutrition(AppDestination.NutritionFlow, "Nutrition", "🍎"),
    Progress(AppDestination.ProgressFlow, "Progress", "📈"),
    Profile(AppDestination.ProfileFlow, "Profile", "👤"),
}

/**
 * Barra de navegacion inferior compartida (UI02): un solo componente para todas las pantallas.
 * La pestana seleccionada sale de la jerarquia del destino actual: cualquier pantalla dentro
 * del grafo "flow_workout" deja marcada la pestana Workout.
 *
 * Estilo de Figma: borde superior de 1 px, activo en verde SemiBold 11 px, inactivo en gris
 * tenue Medium.
 */
@Composable
fun VoraBottomBar(
    currentDestination: NavDestination?,
    onTabClick: (BottomTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    Column(modifier = modifier) {
        HorizontalDivider(thickness = 1.dp, color = colors.outlineVariant)
        NavigationBar(
            containerColor = colors.surface,
            tonalElevation = 0.dp,
            // MainActivity ya aplica los margenes del sistema; sin esto se sumarian dos veces.
            windowInsets = WindowInsets(0, 0, 0, 0),
        ) {
            BottomTab.entries.forEach { tab ->
                val selected = currentDestination?.hierarchy?.any { it.route == tab.flow.route } == true
                NavigationBarItem(
                    selected = selected,
                    onClick = { onTabClick(tab) },
                    icon = { Text(tab.icon, modifier = Modifier.alpha(if (selected) 1f else 0.45f)) },
                    label = {
                        Text(
                            text = tab.label,
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colors.primary,
                        selectedTextColor = colors.primary,
                        unselectedIconColor = colors.outline,
                        unselectedTextColor = colors.outline,
                        indicatorColor = Color.Transparent,
                    ),
                )
            }
        }
    }
}
