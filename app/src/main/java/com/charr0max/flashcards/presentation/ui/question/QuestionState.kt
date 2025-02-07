package com.charr0max.flashcards.presentation.ui.question

data class QuestionState(
    val isLoading: Boolean = false,
    val question: String = "Cargando pregunta...",
    val difficulty: String = "",
    val language: String = "",
    val topics: List<String> = emptyList(),
    val currentTopic: String = "",
    val userAnswer: String = "",
    val listeningState: ListeningState = ListeningState.Idle,
    val geminiAnswer: String = ""
)

enum class ListeningState {
    Idle, Listening, Stopped
}
