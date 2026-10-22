package com.example.parcialtp3.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcialtp3.ui.theme.VoraGreen

/*
 * Estados de una consulta (RF05) pensados para ir DENTRO de VoraScreen, que ya tiene scroll
 * (LoadingIndicator y ErrorMessage de esta carpeta ocupan toda la pantalla y no sirven ahi).
 * Usan los mismos componentes VORA, asi un cambio de estilo se aplica en todas las pantallas.
 */

/** Carga: circulo centrado con altura fija. */
@Composable
fun VoraLoadingBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().height(160.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = VoraGreen)
    }
}

/** Error con boton "Reintentar". */
@Composable
fun VoraErrorBlock(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    VoraCard(modifier = modifier) {
        Text(message, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        VoraPrimaryButton("Reintentar", onRetry)
    }
}

/** Estado vacio (la consulta funciono pero no hay nada). El reintento es opcional. */
@Composable
fun VoraEmptyBlock(message: String, modifier: Modifier = Modifier, onRetry: (() -> Unit)? = null) {
    VoraCard(modifier = modifier) {
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (onRetry != null) {
            VoraTextLink("Reintentar", onRetry)
        }
    }
}

/** Aviso sobre contenido que SI se muestra (por ejemplo, datos guardados porque no hay red). */
@Composable
fun VoraWarningBlock(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    VoraTipCard(modifier = modifier) {
        Text(message, style = MaterialTheme.typography.bodyMedium)
        VoraTextLink("Reintentar", onRetry)
    }
}
