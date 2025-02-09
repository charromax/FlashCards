package com.charr0max.flashcards.presentation.ui.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charr0max.flashcards.data.speech.SpeechRecognizerHelper
import com.charr0max.flashcards.domain.model.Result
import com.charr0max.flashcards.domain.usecase.GetQuestionFromGeminiUseCase
import com.charr0max.flashcards.domain.usecase.SendAnswerToGeminiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private val _snackbarEvents = MutableSharedFlow<String>()
    val snackbarEvents: SharedFlow<String> = _snackbarEvents

    init {
        speechRecognizerHelper.setOnResultListener { result ->
            _state.update {
                it.copy(
                    userAnswer = result
                )
            }
        }
    }

    fun initialize(difficulty: String, topics: List<String>, language: String) {
        _state.value = QuestionState(difficulty = difficulty, topics = topics, language = language)
    }

    fun loadQuestion() = viewModelScope.launch {
        val currentTopic = state.value.topics.random()
        getQuestionFromGeminiUseCase(
            topic = currentTopic,
            difficulty = state.value.difficulty,
            language = state.value.language
        ).collectLatest { result ->
            when (result) {
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _snackbarEvents.emit(result.message)
                }
                Result.Loading -> _state.update {
                    it.copy(
                        isLoading = true,
                        geminiAnswer = "",
                        userAnswer = "",
                        listeningState = ListeningState.Idle
                    )
                }

                is Result.Success -> {
                    _state.update {
                        it.copy(
                            question = result.data ?: "No se pudo obtener la pregunta",
                            isLoading = false,
                            currentTopic = currentTopic
                        )
                    }
                }
            }
        }
    }

    fun sendAnswerToGemini() = viewModelScope.launch {
        sendAnswerToGeminiUseCase(
            topic = state.value.currentTopic,
            difficulty = state.value.difficulty,
            question = state.value.question,
            answer = state.value.userAnswer
        ).collectLatest { result ->
            when (result) {
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _snackbarEvents.emit(result.message)
                }
                Result.Loading -> _state.update { it.copy(isLoading = true) }
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            geminiAnswer = result.data ?: "No se pudo obtener la respuesta",
                            isLoading = false,
                            listeningState = ListeningState.Idle,
                        )
                    }
                }
            }
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