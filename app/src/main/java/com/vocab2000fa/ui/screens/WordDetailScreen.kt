package com.vocab2000fa.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vocab2000fa.data.WordRepository
import com.vocab2000fa.ui.SimpleTopBar
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun WordDetailScreen(
    id: Int,
    repo: WordRepository,
    onBack: () -> Unit
) {
    val w by repo.getById(id).collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    var meaningDraft by remember(id) { mutableStateOf("") }

    // TTS
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(Unit) {
        val engine = TextToSpeech(ctx) { status ->
            if (status == TextToSpeech.SUCCESS) engine.language = Locale.US
        }
        tts = engine
        onDispose { engine.shutdown() }
    }

    LaunchedEffect(w?.meaningFa) {
        meaningDraft = w?.meaningFa ?: ""
    }

    Scaffold(
        topBar = { SimpleTopBar("جزئیات واژه", onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (w == null) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                return@Column
            }

            ElevatedCard(shape = MaterialTheme.shapes.extraLarge) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(w!!.word, style = MaterialTheme.typography.headlineMedium)
                    if (w!!.ipa.isNotBlank()) Text(w!!.ipa, style = MaterialTheme.typography.titleMedium)
                    if (w!!.definitionEn.isNotBlank()) {
                        Text("Definition:", style = MaterialTheme.typography.labelLarge)
                        Text(w!!.definitionEn, style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Text("Definition: —", style = MaterialTheme.typography.bodyMedium)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilledTonalButton(onClick = { tts?.speak(w!!.word, TextToSpeech.QUEUE_FLUSH, null, "word") }) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("تلفظ")
                        }
                        FilledTonalButton(
                            onClick = { scope.launch { repo.setLearned(w!!.id, !w!!.learned) } }
                        ) { Text(if (w!!.learned) "یادگرفته شد ✅" else "یاد گرفتم") }

                        FilledTonalButton(
                            onClick = { scope.launch { repo.setFavorite(w!!.id, !w!!.favorite) } }
                        ) { Text(if (w!!.favorite) "⭐ علاقه‌مندی" else "علاقه‌مندی") }
                    }
                }
            }

            ElevatedCard(shape = MaterialTheme.shapes.extraLarge) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("معنی فارسی", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = meaningDraft,
                        onValueChange = { meaningDraft = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("مثلاً: «آسیب رساندن»، «...».") },
                        singleLine = false,
                        minLines = 2
                    )

                    Button(
                        onClick = { scope.launch { repo.setMeaningFa(w!!.id, meaningDraft.trim()) } },
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("ذخیره معنی")
                    }

                    Text(
                        "این نسخه معنی فارسی آماده‌ی ۲۰۰۰ واژه را داخل دیتاست ندارد؛ از همین بخش می‌توانید معنی‌های خودتان را اضافه کنید.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}