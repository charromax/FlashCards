package com.charr0max.flashcards.domain.repository

interface GeminiRepository {
    suspend fun fetchQuestion(prompt: String): String?
    suspend fun sendAnswer(prompt: String): String?
}