package com.example.parcialtp3.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.parcialtp3.model.Quote
import com.example.parcialtp3.shared.LoadingIndicator
import com.example.parcialtp3.viewmodel.FavoritesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onBackClick: () -> Unit,
    onRemoveClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        // MainActivity ya aplica los margenes del sistema; sin esto se sumarian dos veces.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                actions = { TextButton(onClick = onBackClick) { Text("Volver") } },
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (uiState) {
            FavoritesUiState.Loading -> LoadingIndicator(contentModifier)
            FavoritesUiState.Empty -> EmptyFavorites(contentModifier)
            is FavoritesUiState.Content -> FavoritesList(
                quotes = uiState.quotes,
                onRemoveClick = onRemoveClick,
                modifier = contentModifier,
            )
        }
    }
}

@Composable
private fun FavoritesList(
    quotes: List<Quote>,
    onRemoveClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // key = id: Compose identifica cada fila por su id y no por su posicion.
        // Asi, al borrar una, las demas no se vuelven a armar ni pierden su estado.
        items(items = quotes, key = { it.id }) { quote ->
            FavoriteItem(quote = quote, onRemoveClick = { onRemoveClick(quote.id) })
        }
    }
}

@Composable
private fun FavoriteItem(
    quote: Quote,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "“${quote.text}”",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "${quote.author} · ${quote.category}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp),
            )
            TextButton(
                onClick = onRemoveClick,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Composable
private fun EmptyFavorites(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Aún no guardaste favoritos",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Marcá una frase como favorita y aparecerá acá.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
