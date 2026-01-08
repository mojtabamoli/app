package com.vocab2000fa.data

import kotlinx.coroutines.flow.Flow

class WordRepository(private val dao: WordDao) {

    fun countAll(): Flow<Int> = dao.countAll()
    fun countLearned(): Flow<Int> = dao.countLearned()
    fun countFavorites(): Flow<Int> = dao.countFavorites()

    fun wordsInLesson(level: Int, lesson: Int): Flow<List<WordEntity>> = dao.wordsInLesson(level, lesson)
    fun getById(id: Int): Flow<WordEntity?> = dao.getById(id)
    fun searchPrefix(q: String): Flow<List<WordEntity>> = dao.searchPrefix(q)
    fun favorites(): Flow<List<WordEntity>> = dao.favorites()

    suspend fun setLearned(id: Int, learned: Boolean) =
        dao.setLearned(id, learned, System.currentTimeMillis())

    suspend fun setFavorite(id: Int, favorite: Boolean) =
        dao.setFavorite(id, favorite, System.currentTimeMillis())

    suspend fun setMeaningFa(id: Int, meaningFa: String) =
        dao.setMeaningFa(id, meaningFa, System.currentTimeMillis())

    suspend fun randomWords(n: Int): List<WordEntity> = dao.randomWords(n)
}