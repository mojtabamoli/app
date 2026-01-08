package com.vocab2000fa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocab2000fa.data.WordRepository
import com.vocab2000fa.ui.SimpleTopBar

@Composable
fun LevelsScreen(
    repo: WordRepository,
    onBack: () -> Unit,
    onOpenLevel: (Int) -> Unit
) {
    Scaffold(
        topBar = { SimpleTopBar("سطح‌ها", onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(4) { i ->
                val level = i + 1
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    onClick = { onOpenLevel(level) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("سطح $level", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text("۵۰ درس • ۵۰۰ واژه", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}