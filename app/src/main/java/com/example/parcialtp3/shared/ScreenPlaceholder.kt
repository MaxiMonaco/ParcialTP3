package com.example.parcialtp3.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Un boton del camino de navegacion de una pantalla provisoria. */
data class DemoLink(val label: String, val onClick: () -> Unit)

/**
 * Pantalla provisoria con el estilo de VORA: muestra el nombre del frame de Figma, su estado
 * (funcional / demostracion) y un boton por cada destino al que lleva en el diseno. Sirve para
 * recorrer toda la app desde el dia uno. Quien maqueta la pantalla real reemplaza SOLO el cuerpo
 * de su composable y conserva las lambdas, asi la navegacion no se rompe.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPlaceholder(
    figmaName: String,
    status: String,
    links: List<DemoLink>,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    info: String? = null,
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier,
        containerColor = colors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(figmaName, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    if (onBackClick != null) {
                        TextButton(onClick = onBackClick) {
                            Text("Atrás", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.onBackground,
                ),
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                // Margen de pantalla de 20 px, como en el diseno.
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Etiqueta de estado en forma de pildora, con menta translucida.
            Surface(shape = CircleShape, color = colors.secondaryContainer) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.primary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
            if (info != null) {
                // Tarjeta blanca: radio 16, padding 16, borde sutil.
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = colors.surface,
                    border = BorderStroke(1.dp, colors.outlineVariant),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = info,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
            Text(
                text = "Pantalla provisoria: reemplazar por el diseño de Figma. " +
                    "Los botones son el camino de navegación hacia las otras pantallas.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
            )
            links.forEachIndexed { index, link ->
                if (index == 0) {
                    // Boton principal de Figma: verde, texto blanco 15 Bold, padding 14/24, radio 100.
                    Button(
                        onClick = link.onClick,
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            link.label,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                } else {
                    // Boton secundario de Figma ("Edit workout"): pildora blanca, borde verde fino,
                    // texto oscuro 13 SemiBold.
                    OutlinedButton(
                        onClick = link.onClick,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, colors.primary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = colors.surface,
                            contentColor = colors.onSurface,
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            link.label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}
