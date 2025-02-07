package com.charr0max.flashcards.presentation.ui.question

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.charr0max.flashcards.R
import com.charr0max.flashcards.presentation.ui.util.AppColors
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_JR
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_SENIOR
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_SSR
import com.charr0max.flashcards.presentation.ui.util.PermissionHelper
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel = hiltViewModel(),
    navController: NavController,
    difficulty: String,
    topics: List<String>,
    activity: ComponentActivity
) {
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleListening()
        }
    }

    val state by viewModel.state.collectAsState()

    val backgroundColor = when (state.difficulty) {
        DIFFICULTY_JR -> AppColors.GreenLight
        DIFFICULTY_SSR -> AppColors.YellowLight
        DIFFICULTY_SENIOR -> AppColors.RedLight
        else -> Color.Gray
    }

    var userAnswer by remember { mutableStateOf(TextFieldValue(state.userAnswer)) }

    LaunchedEffect(state.userAnswer) {
        userAnswer = TextFieldValue(state.userAnswer)
    }

    LaunchedEffect(state.geminiAnswer) {
        if (state.geminiAnswer.isNotEmpty()) {
            userAnswer = TextFieldValue(state.geminiAnswer)
        }
    }

    LaunchedEffect(state.listeningState) {
        if (state.listeningState == ListeningState.Stopped) {
            delay(5000)
            viewModel.sendAnswerToGemini()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initialize(difficulty, topics)
        viewModel.loadQuestion()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pregunta nivel ${state.difficulty}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = backgroundColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.isLoading) CircularProgressIndicator() else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                            .padding(8.dp)
                    ) {
                        Text("Temas seleccionados", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                state.topics.forEach { topic ->
                                    if (topic == state.currentTopic) {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(topic.uppercase() + " ")
                                        }
                                    } else {
                                        append("$topic ")
                                    }
                                }
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = state.question, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = userAnswer,
                        onValueChange = { userAnswer = it },
                        label = { Text("Tu respuesta") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        singleLine = false
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { viewModel.loadQuestion() }) {
                        Text("Siguiente Pregunta")
                    }

                    FloatingActionButton(
                        onClick = {
                            if (PermissionHelper.hasAudioPermission(activity)) {
                                viewModel.toggleListening()
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        containerColor = if (state.listeningState == ListeningState.Listening) {
                            Color.Red
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    ) {
                        Icon(
                            imageVector = if (state.listeningState == ListeningState.Listening) {
                                Icons.Filled.Clear
                            } else {
                                ImageVector.vectorResource(R.drawable.microphone)
                            },
                            tint = Color.White,
                            contentDescription = "Iniciar reconocimiento de voz"
                        )
                    }
                }
            }
        }
    }
}
