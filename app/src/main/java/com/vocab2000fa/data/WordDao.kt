package com.vocab2000fa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Query("SELECT COUNT(*) FROM words")
    fun countAll(): Flow<Int>

    @Query("SELECT COUNT(*) FROM words WHERE learned = 1")
    fun countLearned(): Flow<Int>

    @Query("SELECT COUNT(*) FROM words WHERE favorite = 1")
    fun countFavorites(): Flow<Int>

    @Query("SELECT * FROM words WHERE level = :level AND lesson = :lesson ORDER BY rank ASC")
    fun wordsInLesson(level: Int, lesson: Int): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<WordEntity?>

    @Query("SELECT * FROM words WHERE word LIKE :q || '%' ORDER BY rank ASC LIMIT 100")
    fun searchPrefix(q: String): Flow<List<WordEntity>>

    @Query("SELECT * FROM words WHERE favorite = 1 ORDER BY rank ASC")
    fun favorites(): Flow<List<WordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<WordEntity>)

    @Update
    suspend fun update(word: WordEntity)

    @Query("UPDATE words SET learned = :learned, updatedAt = :ts WHERE id = :id")
    suspend fun setLearned(id: Int, learned: Boolean, ts: Long)

    @Query("UPDATE words SET favorite = :favorite, updatedAt = :ts WHERE id = :id")
    suspend fun setFavorite(id: Int, favorite: Boolean, ts: Long)

    @Query("UPDATE words SET meaningFa = :meaningFa, updatedAt = :ts WHERE id = :id")
    suspend fun setMeaningFa(id: Int, meaningFa: String, ts: Long)

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT :n")
    suspend fun randomWords(n: Int): List<WordEntity>
}