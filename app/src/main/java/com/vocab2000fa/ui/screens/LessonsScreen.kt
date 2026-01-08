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
fun LessonsScreen(
    level: Int,
    repo: WordRepository,
    onBack: () -> Unit,
    onOpenLesson: (Int) -> Unit
) {
    val lessons = remember(level) { (1..50).toList() }

    Scaffold(
        topBar = { SimpleTopBar("سطح $level • درس‌ها", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(lessons) { lesson ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    onClick = { onOpenLesson(lesson) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("درس $lesson", style = MaterialTheme.typography.titleMedium)
                        Text("۱۰ واژه", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}