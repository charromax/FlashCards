package com.charr0max.flashcards.domain.usecase

import com.charr0max.flashcards.domain.model.Result
import com.charr0max.flashcards.domain.repository.GeminiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionFromGeminiUseCase  @Inject constructor(
    private val repository: GeminiRepository
) {
    operator fun invoke(topic: String, difficulty: String, language: String): Flow<Result<String?>> {
        val prompt = "Actúa como un entrevistador técnico para un puesto $difficulty. " +
                "Formula una sola pregunta sobre $topic en el contexto del lenguaje $language que evaluarías " +
                "en una entrevista técnica para este nivel de experiencia. " +
                "Evita preguntas abiertas como 'describe una situación en la que tuviste que...' y en su lugar genera preguntas " +
                "con respuestas más concisas y técnicas, que puedan evaluarse de manera objetiva."
        return repository.talkToGemini(prompt)
    }
}