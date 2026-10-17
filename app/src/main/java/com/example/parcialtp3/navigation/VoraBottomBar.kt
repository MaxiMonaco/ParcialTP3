package com.example.parcialtp3.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.R
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

/** Las 5 pestanas de Figma. El icono es un vector en res/drawable (se tiñe segun la pestana activa). */
enum class BottomTab(val flow: AppDestination, val label: String, @DrawableRes val icon: Int) {
    Home(AppDestination.HomeFlow, "Home", R.drawable.ic_home),
    Workout(AppDestination.WorkoutFlow, "Workout", R.drawable.ic_workout),
    Nutrition(AppDestination.NutritionFlow, "Nutrition", R.drawable.ic_nutrition),
    Progress(AppDestination.ProgressFlow, "Progress", R.drawable.ic_progress),
    Profile(AppDestination.ProfileFlow, "Profile", R.drawable.ic_profile),
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
                    icon = { Icon(painterResource(tab.icon), contentDescription = tab.label, modifier = Modifier.size(24.dp).alpha(if (selected) 1f else 0.6f)) },
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
