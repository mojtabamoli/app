package com.vocab2000fa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocab2000fa.data.WordRepository
import com.vocab2000fa.ui.SimpleTopBar

@Composable
fun FavoritesScreen(
    repo: WordRepository,
    onBack: () -> Unit,
    onOpenWord: (Int) -> Unit
) {
    val items by repo.favorites().collectAsState(initial = emptyList())

    Scaffold(topBar = { SimpleTopBar("علاقه‌مندی‌ها", onBack) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items) { w ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    onClick = { onOpenWord(w.id) }
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(w.word, style = MaterialTheme.typography.titleMedium)
                        if (w.meaningFa.isNotBlank()) Text(w.meaningFa)
                        else Text("معنی فارسی: —", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}