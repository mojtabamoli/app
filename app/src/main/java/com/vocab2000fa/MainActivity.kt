package com.vocab2000fa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.vocab2000fa.data.AppDatabase
import com.vocab2000fa.data.Seeder
import com.vocab2000fa.ui.AppRoot
import com.vocab2000fa.ui.theme.Vocab2000Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.get(this)

        // Seed database (idempotent via REPLACE).
        lifecycleScope.launch { Seeder.seedIfEmpty(this@MainActivity, db) }

        setContent {
            Vocab2000Theme {
                AppRoot(db = db)
            }
        }
    }
}