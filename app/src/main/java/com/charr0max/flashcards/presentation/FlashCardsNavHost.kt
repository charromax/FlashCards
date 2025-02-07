package com.charr0max.flashcards.presentation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.charr0max.flashcards.presentation.ui.home.HomeScreen
import com.charr0max.flashcards.presentation.ui.question.QuestionScreen
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_ARG
import com.charr0max.flashcards.presentation.ui.util.Constants.DIFFICULTY_JR
import com.charr0max.flashcards.presentation.ui.util.Constants.HOME_ROUTE
import com.charr0max.flashcards.presentation.ui.util.Constants.QUESTION_ROUTE
import com.charr0max.flashcards.presentation.ui.util.Constants.TOPICS_ARG
import androidx.navigation.compose.NavHost as ComposeNavHost

interface NavigationRoute {
    val route: String
    val arguments: List<NamedNavArgument> get() = emptyList()
}

data object HomeNavigation : NavigationRoute {
    override val route: String = HOME_ROUTE
}

data object QuestionNavigation : NavigationRoute {
    override val route: String = QUESTION_ROUTE
}

@Composable
fun FlashCardsNavHost(activity: ComponentActivity) {
    val navController = rememberNavController()

    ComposeNavHost(
        navController = navController,
        startDestination = HomeNavigation.route
    ) {

        composable(HomeNavigation.route) { HomeScreen(navController) }

        composable(QuestionNavigation.route) { backStackEntry ->
            val difficulty = backStackEntry.arguments?.getString(DIFFICULTY_ARG) ?: DIFFICULTY_JR
            val topics = backStackEntry.arguments?.getString(TOPICS_ARG)?.split(",") ?: emptyList()
            QuestionScreen(
                navController = navController,
                difficulty = difficulty,
                topics = topics,
                activity = activity
            )
        }
    }
}