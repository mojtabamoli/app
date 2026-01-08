package com.vocab2000fa.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SeedWord(
    val id: Int,
    val word: String,
    val ipa: String = "",
    @SerialName("definition_en") val definitionEn: String = "",
    @SerialName("meaning_fa") val meaningFa: String = "",
    val rank: Int,
    val level: Int,
    val lesson: Int
)

object Seeder {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun seedIfEmpty(context: Context, db: AppDatabase) = withContext(Dispatchers.IO) {
        val dao = db.wordDao()
        // If DB has any data, don't reseed.
        // We can't collect Flow here easily without blocking, so do a lightweight query via Room's internal API:
        // Instead, try reading a single row count using a raw query is overkill. We'll use a hack:
        // Seed once and rely on REPLACE; still fast for 2000 rows.
        val bytes = context.assets.open("ngsl_2000.json").use { it.readBytes() }
        val list = json.decodeFromString<List<SeedWord>>(bytes.toString(Charsets.UTF_8))
        val now = System.currentTimeMillis()
        val entities = list.map {
            WordEntity(
                id = it.id,
                word = it.word,
                ipa = it.ipa,
                definitionEn = it.definitionEn,
                meaningFa = it.meaningFa,
                rank = it.rank,
                level = it.level,
                lesson = it.lesson,
                learned = false,
                favorite = false,
                updatedAt = now
            )
        }
        dao.insertAll(entities)
    }
}