package com.charr0max.flashcards.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.charr0max.flashcards.presentation.ui.util.AppColors
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_JR
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_SENIOR
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_SSR
import com.charr0max.flashcards.presentation.ui.util.Constants.QUESTION_ROUTE
import com.charr0max.flashcards.presentation.ui.util.Constants.TOPIC_COROUTINES
import com.charr0max.flashcards.presentation.ui.util.Constants.TOPIC_FLOWS
import com.charr0max.flashcards.presentation.ui.util.Constants.TOPIC_JETPACK_COMPOSE
import com.charr0max.flashcards.presentation.ui.util.Constants.TOPIC_NETWORKING
import com.charr0max.flashcards.presentation.ui.util.Constants.TOPIC_PERFORMANCE
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(navController: NavController) {

    val difficulties = listOf(
        DIFFICULTY_JR to (AppColors.GreenLight to AppColors.GreenPrimary),
        DIFFICULTY_SSR to (AppColors.YellowLight to AppColors.YellowPrimary),
        DIFFICULTY_SENIOR to (AppColors.RedLight to AppColors.RedPrimary)
    )

    val topics = listOf(
        TOPIC_JETPACK_COMPOSE,
        TOPIC_PERFORMANCE,
        TOPIC_COROUTINES,
        TOPIC_NETWORKING,
        TOPIC_FLOWS
    )
    val selectedTopics = remember {
        mutableStateMapOf<String, Boolean>().apply {
            topics.forEach {
                put(
                    it,
                    false
                )
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    )
    { pv ->
        HomeScreenContent(
            pv,
            difficulties,
            topics,
            selectedTopics,
            snackbarHostState
        ) { selectedTopicsList, selectedDifficulty ->
            navController.navigate(
                QUESTION_ROUTE
                    .replace("{difficulty}", selectedDifficulty)
                    .replace("{topics}", selectedTopicsList)
            )
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun HomeScreenContent(
    pv: PaddingValues,
    difficulties: List<Pair<String, Pair<Color, Color>>>,
    topics: List<String>,
    selectedTopics: SnapshotStateMap<String, Boolean>,
    snackbarHostState: SnackbarHostState,
    navigateToQuestions: (selectedTopicList: String, selectedDifficulty: String) -> Unit
) {
    var selectedDifficulty by remember { mutableStateOf("Jr") }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Selecciona la dificultad", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                difficulties.forEach { (label, colors) ->
                    val isSelected = selectedDifficulty == label
                    FilterChip(
                        label = label,
                        selected = isSelected,
                        colorDefault = colors.first,
                        colorSelected = colors.second,
                        onClick = { selectedDifficulty = label }
                    )
                }
            }

            Text("Selecciona los temas", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                topics.forEach { topic ->
                    FilterChip(
                        label = topic,
                        selected = selectedTopics[topic] == true,
                        onClick = { selectedTopics[topic] = !(selectedTopics[topic] ?: false) }
                    )
                }
            }
        }

        Button(
            onClick = {
                val selectedTopicsList = selectedTopics.filterValues { it }.keys.joinToString(",")
                if (selectedTopicsList.isEmpty()) {
                    coroutineScope.launch { snackbarHostState.showSnackbar("Debes seleccionar al menos un tema.") }
                } else {
                    navigateToQuestions(selectedTopicsList, selectedDifficulty)
                }
            }) {
            Text("Start")
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    colorDefault: Color = Color.LightGray,
    colorSelected: Color = Color.Gray,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .toggleable(value = selected, onValueChange = { onClick() }),
        shape = MaterialTheme.shapes.medium,
        color = if (selected) colorSelected else colorDefault
    ) {
        Text(text = label, modifier = Modifier.padding(8.dp))
    }
}
