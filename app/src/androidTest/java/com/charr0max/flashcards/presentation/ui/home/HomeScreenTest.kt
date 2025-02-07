package com.charr0max.flashcards.presentation.ui.home

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.charr0max.flashcards.presentation.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        composeTestRule.activity.runOnUiThread {
            navController = TestNavHostController(composeTestRule.activity).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                setGraph(createTestNavGraph(this), null) // ✅ Asigna el NavGraph correctamente
            }
        }
        composeTestRule.activity.setContent {
            HomeScreen(navController = navController)
        }
    }

    /** Crea un NavGraph válido para los tests */
    private fun createTestNavGraph(navController: NavController): NavGraph {
        return navController.createGraph(startDestination = "home") {
            composable(route = "home") { HomeScreen(navController) }
            composable(route = "question/{difficulty}/{topics}/{language}") { }
        }
    }

    @Test
    fun whenUserSelectsOptions_andClicksStart_argumentsArePassedCorrectly() {
        // Seleccionar dificultad "Ssr"
        composeTestRule.onNodeWithText("Ssr").performClick()

        // Seleccionar "Coroutines" como topic
        composeTestRule.onNodeWithText("Coroutines").performClick()

        // Seleccionar "Kotlin" como lenguaje
        composeTestRule.onNodeWithText("Kotlin").performClick()

        // Presionar el botón "Start"
        composeTestRule.onNodeWithText("Start").performClick()

        composeTestRule.waitForIdle()

        // Obtener argumentos reales del backStackEntry
        val backStackEntry = navController.currentBackStackEntry
        val args = backStackEntry?.arguments

        assert(args?.getString("difficulty") == "Ssr")
        assert(args?.getString("topics") == "Coroutines")
        assert(args?.getString("language") == "Kotlin")
    }
}