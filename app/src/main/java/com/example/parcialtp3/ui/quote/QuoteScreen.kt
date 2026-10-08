package com.example.parcialtp3.ui.quote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parcialtp3.model.Quote
import com.example.parcialtp3.shared.ErrorMessage
import com.example.parcialtp3.shared.LoadingIndicator
import com.example.parcialtp3.viewmodel.QuoteUiState

/**
 * Pantalla Frase. No sabe nada de ViewModels ni de navegacion: recibe el estado
 * y avisa los eventos con lambdas. Asi es facil de probar y de previsualizar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(
    uiState: QuoteUiState,
    onFavoritesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onNextQuote: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        // MainActivity ya aplica los margenes del sistema; sin esto se sumarian dos veces.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text("Frases") },
                actions = {
                    TextButton(onClick = onFavoritesClick) { Text("Favoritos") }
                    TextButton(onClick = onLogoutClick) { Text("Salir") }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        // "when" sobre una sealed interface: tiene que cubrir los 3 estados.
        when (uiState) {
            QuoteUiState.Loading -> LoadingIndicator(contentModifier)
            is QuoteUiState.Error -> ErrorMessage(
                message = uiState.message,
                onRetry = onRetry,
                modifier = contentModifier,
            )
            is QuoteUiState.Content -> QuoteContent(
                quote = uiState.quote,
                isFavorite = uiState.isFavorite,
                onToggleFavorite = onToggleFavorite,
                onNextQuote = onNextQuote,
                modifier = contentModifier,
            )
        }
    }
}

@Composable
private fun QuoteContent(
    quote: Quote,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onNextQuote: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        // verticalScroll va ANTES del padding, asi el margen tambien se desplaza con el contenido.
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "“${quote.text}”",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // weight(1f): autor y categoria ocupan el espacio sobrante y la estrella queda a la derecha.
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = quote.author,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = quote.category,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    // El color cambia solo segun isFavorite, que viene de Room.
                    IconButton(onClick = onToggleFavorite) {
                        Text(
                            text = "★",
                            fontSize = 32.sp,
                            color = if (isFavorite) Color(0xFFFFC107) else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.semantics {
                                contentDescription = if (isFavorite) "Quitar favorito" else "Guardar favorito"
                            },
                        )
                    }
                }
            }
        }
        OutlinedButton(
            onClick = onNextQuote,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        ) {
            Text("Otra frase")
        }
    }
}
