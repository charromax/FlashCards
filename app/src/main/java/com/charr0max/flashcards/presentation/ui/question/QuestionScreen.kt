package com.charr0max.flashcards.presentation.ui.question

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.charr0max.flashcards.R
import com.charr0max.flashcards.presentation.ui.util.AppColors
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_JR
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_SENIOR
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_SSR
import com.charr0max.flashcards.presentation.ui.util.LinkParser
import com.charr0max.flashcards.presentation.ui.util.PermissionHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel = hiltViewModel(),
    navController: NavController,
    difficulty: String,
    language: String,
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

    val state by viewModel.state.collectAsStateWithLifecycle()

    val backgroundColor = when (state.difficulty) {
        DIFFICULTY_JR -> AppColors.GreenLight
        DIFFICULTY_SSR -> AppColors.YellowLight
        DIFFICULTY_SENIOR -> AppColors.RedLight
        else -> Color.Gray
    }

    var userAnswer by remember { mutableStateOf(TextFieldValue(state.userAnswer)) }
    var geminiAnswer by remember { mutableStateOf(AnnotatedString("")) }
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    var isUserTyping by remember { mutableStateOf(true) }

    LaunchedEffect(state.geminiAnswer) {
        if (state.geminiAnswer.isNotEmpty()) {
            val rawText = state.geminiAnswer
            val links = LinkParser.extractLinks(rawText)
            val cleanText = LinkParser.cleanMarkdownLinks(rawText)

            val annotatedString = AnnotatedString.Builder(cleanText)
            links.forEach { link ->
                val startIndex = cleanText.indexOf(link)
                if (startIndex != -1) {
                    annotatedString.addStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        ),
                        start = startIndex,
                        end = startIndex + link.length
                    )
                    annotatedString.addStringAnnotation(
                        tag = "URL",
                        annotation = link,
                        start = startIndex,
                        end = startIndex + link.length
                    )
                }
            }
            geminiAnswer = annotatedString.toAnnotatedString()
            isUserTyping = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initialize(difficulty, topics, language)
        viewModel.loadQuestion()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pregunta sobre ${state.language}") },
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
                    Box(
                        modifier = Modifier
                            .heightIn(max = 300.dp)
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    ) {
                        Text(
                            text = state.question,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isUserTyping) {
                        OutlinedTextField(
                            value = userAnswer.text,
                            onValueChange = {
                                userAnswer = TextFieldValue(it)
                                isUserTyping =
                                    true  // 🔹 Usuario está escribiendo, deshabilitamos links
                            },
                            label = { Text("Tu respuesta") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            singleLine = false
                        )
                    } else {
                        ClickableText(
                            text = geminiAnswer,
                            onClick = { offset ->
                                geminiAnswer.getStringAnnotations("URL", offset, offset)
                                    .firstOrNull()?.let { annotation ->
                                        uriHandler.openUri(annotation.item)
                                    }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        isUserTyping = true
                        userAnswer = TextFieldValue("")
                        geminiAnswer = AnnotatedString("")
                        viewModel.loadQuestion()
                    }) {
                        Text("Siguiente Pregunta")
                    }

                    Row {
                        ActionButton(
                            onClick = {
                                if (PermissionHelper.hasAudioPermission(activity)) {
                                    viewModel.toggleListening()
                                } else {
                                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            containerColor = if (state.listeningState == ListeningState.Listening) {
                                Color.Red
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            enabled = isUserTyping
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
                        Spacer(modifier = Modifier.width(16.dp))
                        ActionButton(
                            onClick = viewModel::sendAnswerToGemini,
                            enabled = isUserTyping && userAnswer.text.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                tint = Color.White,
                                contentDescription = "Enviar respuesta"
                            )
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun ActionButton(
    modifier: Modifier = Modifier.size(56.dp),
    containerColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
    enabled: Boolean,
    icon: @Composable () -> Unit,
) {
    FloatingActionButton(
        onClick = {
            if (enabled) onClick()
        },
        containerColor = if (enabled) containerColor else Color.LightGray,
        modifier = modifier,
        content = icon,
    )
}
