package com.vocab2000fa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: Int,          // same as rank
    val word: String,
    val ipa: String,
    val definitionEn: String,
    val meaningFa: String,
    val rank: Int,
    val level: Int,
    val lesson: Int,
    val learned: Boolean = false,
    val favorite: Boolean = false,
    val updatedAt: Long = 0L
)