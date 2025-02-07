package com.charr0max.flashcards.domain.usecase

import com.charr0max.flashcards.domain.repository.GeminiRepository
import javax.inject.Inject

class GetQuestionFromGeminiUseCase  @Inject constructor(
    private val repository: GeminiRepository
) {
    suspend operator fun invoke(topic: String, difficulty: String): String {
        val prompt = "Actúa como un entrevistador técnico para un puesto $difficulty. " +
                "Formula una sola pregunta sobre $topic que evaluarías " +
                "en una entrevista técnica para este nivel de experiencia."
        return repository.fetchQuestion(prompt) ?: "No se pudo obtener una pregunta."
    }
}