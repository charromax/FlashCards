package com.charr0max.flashcards.presentation.ui.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charr0max.flashcards.data.speech.SpeechRecognizerHelper
import com.charr0max.flashcards.domain.usecase.GetQuestionFromGeminiUseCase
import com.charr0max.flashcards.domain.usecase.SendAnswerToGeminiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionFromGeminiUseCase: GetQuestionFromGeminiUseCase,
    private val sendAnswerToGeminiUseCase: SendAnswerToGeminiUseCase,
    private val speechRecognizerHelper: SpeechRecognizerHelper
) : ViewModel() {

    private val _state = MutableStateFlow(QuestionState())
    val state: StateFlow<QuestionState> get() = _state

    init {
        speechRecognizerHelper.setOnResultListener { result ->
            _state.update {
                it.copy(
                    userAnswer = result
                )
            }
        }
    }

    fun initialize(difficulty: String, topics: List<String>) {
        _state.value = QuestionState(difficulty = difficulty, topics = topics)
    }

    fun loadQuestion() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    geminiAnswer = "",
                    userAnswer = "",
                    listeningState = ListeningState.Idle
                )
            }
            _state.update {
                val currentTopic = it.topics.random()
                it.copy(
                    question = getQuestionFromGeminiUseCase(
                        topic = currentTopic,
                        difficulty = it.difficulty
                    ),
                    currentTopic = currentTopic,
                    isLoading = false
                )
            }
        }
    }

    fun sendAnswerToGemini() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        _state.update {
            it.copy(
                geminiAnswer = sendAnswerToGeminiUseCase(
                    topic = it.currentTopic,
                    difficulty = it.difficulty,
                    question = it.question,
                    answer = it.userAnswer
                ),
                isLoading = false,
                listeningState = ListeningState.Idle,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizerHelper.destroy()
    }

    fun toggleListening() {
        val newState = when (_state.value.listeningState) {
            ListeningState.Idle, ListeningState.Stopped -> ListeningState.Listening
            ListeningState.Listening -> ListeningState.Stopped
        }

        _state.update { it.copy(listeningState = newState) }

        if (newState == ListeningState.Listening) {
            speechRecognizerHelper.startListening()
        } else {
            speechRecognizerHelper.stopListening()
        }
    }
}