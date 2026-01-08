package com.vocab2000fa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocab2000fa.data.WordRepository
import kotlinx.coroutines.flow.combine

@Composable
fun HomeScreen(
    repo: WordRepository,
    onOpenLevels: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenQuiz: () -> Unit
) {
    val total by repo.countAll().collectAsState(initial = 2000)
    val learned by repo.countLearned().collectAsState(initial = 0)
    val favorites by repo.countFavorites().collectAsState(initial = 0)

    val progress = if (total == 0) 0f else learned.toFloat() / total.toFloat()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("واژگان ۲۰۰۰") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(progress = { progress })
                    Column(modifier = Modifier.weight(1f)) {
                        Text("پیشرفت", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text("$learned از $total واژه", style = MaterialTheme.typography.bodyMedium)
                        Text("$favorites مورد علاقه", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Button(
                onClick = onOpenLevels,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) { Text("شروع درس‌ها (۴ سطح)") }

            OutlinedButton(
                onClick = onOpenSearch,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) { Text("جستجو") }

            OutlinedButton(
                onClick = onOpenFavorites,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) { Text("علاقه‌مندی‌ها") }

            OutlinedButton(
                onClick = onOpenQuiz,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) { Text("کوییز سریع") }

            Spacer(Modifier.weight(1f))

            Text(
                "تعریف انگلیسی از NGSL و فونتیک IPA به‌صورت تقریبی تولید شده است.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}