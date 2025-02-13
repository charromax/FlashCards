package com.charr0max.flashcards.presentation.ui.question

import app.cash.turbine.test
import com.charr0max.flashcards.MainDispatcherRule
import com.charr0max.flashcards.data.speech.SpeechRecognizerHelper
import com.charr0max.flashcards.domain.model.Result
import com.charr0max.flashcards.domain.usecase.GetQuestionFromGeminiUseCase
import com.charr0max.flashcards.domain.usecase.SendAnswerToGeminiUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QuestionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule() // Custom rule to swap Dispatchers.Main for testing

    private lateinit var viewModel: QuestionViewModel

    private lateinit var getQuestionFromGeminiUseCase: GetQuestionFromGeminiUseCase
    private lateinit var sendAnswerToGeminiUseCase: SendAnswerToGeminiUseCase
    private lateinit var speechRecognizerHelper: SpeechRecognizerHelper

    @Before
    fun setUp() {
        getQuestionFromGeminiUseCase = mockk()
        sendAnswerToGeminiUseCase = mockk()
        speechRecognizerHelper = mockk(relaxed = true) // Automatically handles unused calls

        viewModel = QuestionViewModel(
            getQuestionFromGeminiUseCase,
            sendAnswerToGeminiUseCase,
            speechRecognizerHelper
        )
    }

    @Test
    fun `initialize sets correct initial state`() = runTest {
        viewModel.initialize("Ssr", listOf("Coroutines", "Compose"), "Kotlin")

        assertEquals("Ssr", viewModel.state.value.difficulty)
        assertEquals(listOf("Coroutines", "Compose"), viewModel.state.value.topics)
        assertEquals("Kotlin", viewModel.state.value.language)
    }

    @Test
    fun `loadQuestion updates state correctly`() = runTest {
        val testQuestion = "What is Jetpack Compose?"
        val testTopic = "Compose"
        every { getQuestionFromGeminiUseCase(any(), any(), any()) } returns flow {
            emit(Result.Loading) // Simulate initial loading state
            delay(500) // Simulate API delay
            emit(Result.Success("What is Jetpack Compose?"))
        }
        viewModel.initialize("Ssr", listOf(testTopic), "Kotlin")
        viewModel.loadQuestion()

        viewModel.state.test {
            // ✅ Skip the default state (QuestionState default values)
            skipItems(1)

            assertEquals(
                QuestionState(
                    isLoading = true, // ✅ First update should be loading
                    question = "Cargando pregunta...",
                    difficulty = "Ssr",
                    language = "Kotlin",
                    topics = listOf("Compose"),
                    listeningState = ListeningState.Idle
                ), awaitItem()
            )

            assertEquals(
                QuestionState(
                    isLoading = false, // ✅ Now loading is done
                    question = testQuestion, // ✅ Question updated
                    difficulty = "Ssr",
                    language = "Kotlin",
                    topics = listOf("Compose"),
                    currentTopic = testTopic
                ), awaitItem()
            )
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}