package com.charr0max.flashcards.domain.usecase

import com.charr0max.flashcards.domain.repository.GeminiRepository
import javax.inject.Inject

class SendAnswerToGeminiUseCase @Inject constructor(
    private val repository: GeminiRepository
) {
    suspend operator fun invoke(
        topic: String,
        difficulty: String,
        question: String,
        answer: String
    ): String {
        val prompt = "Eres un entrevistador técnico para un puesto de $difficulty. Acabas de preguntar: \"$question\".\n" +
                "El entrevistado respondió lo siguiente sobre $topic:\n\n" +
                "\"$answer\"\n\n" +
                "Evalúa la respuesta considerando el nivel del puesto y proporciona una retroalimentación clara en no más de 6 líneas. " +
                "Indica si la respuesta es correcta, parcial o incorrecta y explica brevemente por qué.\n\n" +
                "Si es relevante, proporciona enlaces a documentación oficial para mejorar la comprensión."
        return repository.fetchQuestion(prompt) ?: "No se pudo obtener una respuesta."
    }
}