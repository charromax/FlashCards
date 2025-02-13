package com.charr0max.flashcards.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.charr0max.flashcards.presentation.theme.FlashCardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlashCardsNavHost(this, innerPadding)
                }
            }
        }
    }
}
