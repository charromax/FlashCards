package com.charr0max.flashcards.data.repository

import android.util.Log
import com.charr0max.flashcards.BuildConfig
import com.charr0max.flashcards.data.model.Content
import com.charr0max.flashcards.data.model.GeminiRequest
import com.charr0max.flashcards.data.model.Part
import com.charr0max.flashcards.data.remote.AIService
import com.charr0max.flashcards.domain.repository.GeminiRepository
import kotlinx.serialization.json.Json

class GeminiRepositoryImpl(private val service: AIService): GeminiRepository {
    override suspend fun fetchQuestion(prompt: String): String? {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(listOf(Part(prompt)))
                )
            )
            val response = service.talkToGemini(
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = request
            )
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun sendAnswer(prompt: String): String? {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(listOf(Part(prompt)))
                )
            )
            val response = service.talkToGemini(
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = request
            )
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}