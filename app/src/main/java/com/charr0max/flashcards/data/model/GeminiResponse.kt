package com.charr0max.flashcards.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    @SerialName("candidates") val candidates: List<Candidate>?
)

@Serializable
data class Candidate(
    @SerialName("content") val content: Content?
)