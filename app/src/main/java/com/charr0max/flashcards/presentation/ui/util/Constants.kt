package com.charr0max.flashcards.presentation.ui.util

object Constants {
    const val HOME_ROUTE = "home"
    const val DIFFICULTY_ARG = "difficulty"
    const val TOPICS_ARG = "topics"
    const val QUESTION_ROUTE = "question/{$DIFFICULTY_ARG}/{$TOPICS_ARG}"

    // Dificultades
    const val DIFFICULTY_JR = "Jr"
    const val DIFFICULTY_SSR = "Ssr"
    const val DIFFICULTY_SENIOR = "Senior"

    // Temas
    const val TOPIC_JETPACK_COMPOSE = "Jetpack Compose"
    const val TOPIC_COROUTINES = "Coroutines"
    const val TOPIC_NETWORKING = "Networking"
    const val TOPIC_PERFORMANCE = "Performance"
    const val TOPIC_FLOWS = "Flows"
}