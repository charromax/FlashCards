package com.charr0max.flashcards.data.repository

import com.charr0max.flashcards.BuildConfig
import com.charr0max.flashcards.data.model.Content
import com.charr0max.flashcards.data.model.GeminiRequest
import com.charr0max.flashcards.data.model.Part
import com.charr0max.flashcards.data.remote.AIService
import com.charr0max.flashcards.domain.model.Result
import com.charr0max.flashcards.domain.repository.GeminiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeminiRepositoryImpl(private val service: AIService) : GeminiRepository {

    override fun fetchQuestion(prompt: String): Flow<Result<String?>> = flow {
        emit(Result.Loading)
        try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(listOf(Part(prompt)))
                )
            )
            val response = service.talkToGemini(
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = request
            )
            emit(Result.Success(response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.localizedMessage ?: "Error desconocido"))
        }
    }

    override fun evaluateAnswer(
        prompt: String,
    ): Flow<Result<String?>> = flow {
        emit(Result.Loading)
        try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(listOf(Part(prompt)))
                )
            )
            val response = service.talkToGemini(
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = request
            )
            emit(Result.Success(response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.localizedMessage ?: "Error desconocido"))
        }
    }
}