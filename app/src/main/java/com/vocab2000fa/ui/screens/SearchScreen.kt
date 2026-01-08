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
fun SearchScreen(
    repo: WordRepository,
    onBack: () -> Unit,
    onOpenWord: (Int) -> Unit
) {
    var q by remember { mutableStateOf("") }
    val results by repo.searchPrefix(q.trim().lowercase()).collectAsState(initial = emptyList())

    Scaffold(topBar = { SimpleTopBar("جستجو", onBack) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = q,
                onValueChange = { q = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("مثلاً: th, go, take ...") },
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results) { w ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
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
}