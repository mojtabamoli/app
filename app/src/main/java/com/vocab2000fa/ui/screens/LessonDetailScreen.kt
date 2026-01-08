package com.vocab2000fa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocab2000fa.data.WordEntity
import com.vocab2000fa.data.WordRepository
import com.vocab2000fa.ui.SimpleTopBar
import kotlinx.coroutines.launch

@Composable
fun LessonDetailScreen(
    level: Int,
    lesson: Int,
    repo: WordRepository,
    onBack: () -> Unit,
    onOpenWord: (Int) -> Unit
) {
    val words by repo.wordsInLesson(level, lesson).collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { SimpleTopBar("سطح $level • درس $lesson", onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(words) { w ->
                WordRow(
                    w = w,
                    onClick = { onOpenWord(w.id) },
                    onToggleLearned = { learned ->
                        scope.launch { repo.setLearned(w.id, learned) }
                    },
                    onToggleFavorite = { fav ->
                        scope.launch { repo.setFavorite(w.id, fav) }
                    }
                )
            }
        }
    }
}

@Composable
private fun WordRow(
    w: WordEntity,
    onClick: () -> Unit,
    onToggleLearned: (Boolean) -> Unit,
    onToggleFavorite: (Boolean) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(w.word, style = MaterialTheme.typography.titleLarge)
                if (w.ipa.isNotBlank()) {
                    Text(w.ipa, style = MaterialTheme.typography.bodyMedium)
                }
                if (w.meaningFa.isNotBlank()) {
                    Text(w.meaningFa, style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text("معنی فارسی: —", style = MaterialTheme.typography.bodySmall)
                }
            }

            IconButton(onClick = { onToggleLearned(!w.learned) }) {
                Icon(
                    if (w.learned) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = "Learned"
                )
            }
            IconButton(onClick = { onToggleFavorite(!w.favorite) }) {
                Icon(
                    if (w.favorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}