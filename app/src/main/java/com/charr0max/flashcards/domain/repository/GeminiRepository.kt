package com.charr0max.flashcards.domain.repository

import com.charr0max.flashcards.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface GeminiRepository {
    fun talkToGemini(prompt: String): Flow<Result<String?>>
}