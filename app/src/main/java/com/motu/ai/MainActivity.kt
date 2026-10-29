package com.motu.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.motu.ai.ui.screens.HomeScreen
import com.motu.ai.ui.theme.MotuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotuTheme {
                HomeScreen()
            }
        }
    }
}
