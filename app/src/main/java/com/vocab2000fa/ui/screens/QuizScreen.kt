package com.vocab2000fa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocab2000fa.data.WordEntity
import com.vocab2000fa.data.WordRepository
import com.vocab2000fa.ui.SimpleTopBar
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun QuizScreen(
    repo: WordRepository,
    onBack: () -> Unit,
    onOpenWord: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }
    var prompt by remember { mutableStateOf("") }
    var options by remember { mutableStateOf<List<WordEntity>>(emptyList()) }
    var answerId by remember { mutableStateOf<Int?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }

    fun nextQuestion() {
        loading = true
        feedback = null
        scope.launch {
            val list = repo.randomWords(4)
            if (list.size < 4) return@launch
            val ans = list.random()
            prompt = if (ans.definitionEn.isNotBlank()) ans.definitionEn else "Choose the correct word."
            options = list.shuffled()
            answerId = ans.id
            loading = false
        }
    }

    LaunchedEffect(Unit) { nextQuestion() }

    Scaffold(topBar = { SimpleTopBar("کوییز سریع", onBack) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                Text("در حال آماده‌سازی سوال...")
                return@Column
            }

            ElevatedCard(shape = MaterialTheme.shapes.extraLarge) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("تعریف (EN):", style = MaterialTheme.typography.labelLarge)
                    Text(prompt, style = MaterialTheme.typography.bodyLarge)
                }
            }

            options.forEach { w ->
                Button(
                    onClick = {
                        val ok = (w.id == answerId)
                        feedback = if (ok) "درست ✅" else "اشتباه ❌"
                        if (!ok) {
                            // keep
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) { Text(w.word) }
            }

            feedback?.let {
                Text(it, style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = { answerId?.let(onOpenWord) }) { Text("باز کردن جواب") }
                    Button(onClick = { nextQuestion() }) { Text("سوال بعدی") }
                }
            }

            Spacer(Modifier.weight(1f))
            Text("این کوییز بر اساس تعریف انگلیسی کار می‌کند.", style = MaterialTheme.typography.bodySmall)
        }
    }
}