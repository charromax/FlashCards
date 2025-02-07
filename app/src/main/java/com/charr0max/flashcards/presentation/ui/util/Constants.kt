package com.charr0max.flashcards.presentation.ui.util

object Constants {
    const val HOME_ROUTE = "home"
    const val DIFFICULTY_ARG = "difficulty"
    const val TOPICS_ARG = "topics"
    const val LANGUAGE_ARG = "language"
    const val QUESTION_ROUTE = "question/{$DIFFICULTY_ARG}/{$TOPICS_ARG}/{$LANGUAGE_ARG}"

    // Dificultades
    const val DIFFICULTY_JR = "Jr"
    const val DIFFICULTY_SSR = "Ssr"
    const val DIFFICULTY_SENIOR = "Senior"

    // Lenguajes
    const val LANGUAGE_KOTLIN = "Kotlin"
    const val LANGUAGE_JAVA = "Java"
    const val LANGUAGE_JAVASCRIPT = "JS"
    const val LANGUAGE_PYTHON = "Python"
    const val LANGUAGE_SWIFT = "Swift"
    const val LANGUAGE_CPP = "C++"

    // Temas
    const val TOPIC_JETPACK_COMPOSE = "Jetpack Compose"
    const val TOPIC_COROUTINES = "Coroutines"
    const val TOPIC_NETWORKING = "Networking"
    const val TOPIC_PERFORMANCE = "Performance"
    const val TOPIC_FLOWS = "Flows"
    const val TOPIC_SWIFTUI = "SwiftUI"
    const val TOPIC_TESTING = "Testing"
}