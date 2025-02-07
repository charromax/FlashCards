package com.charr0max.flashcards.data.remote

import com.charr0max.flashcards.data.model.GeminiRequest
import com.charr0max.flashcards.data.model.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AIService {
    @Headers("Content-Type: application/json")
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun talkToGemini(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}