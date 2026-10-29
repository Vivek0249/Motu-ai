package com.motu.ai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val MotuCyan = Color(0xFF00E5FF)
val MotuBlue = Color(0xFF1E88E5)
val MotuBackground = Color(0xFF05080F)
val MotuSurface = Color(0xFF0B1220)
val MotuSurfaceVariant = Color(0xFF111C33)

private val MotuColorScheme = darkColorScheme(
    primary = MotuCyan,
    onPrimary = Color(0xFF00363D),
    secondary = MotuBlue,
    onSecondary = Color.White,
    background = MotuBackground,
    onBackground = Color(0xFFE6F6FF),
    surface = MotuSurface,
    onSurface = Color(0xFFE6F6FF),
    surfaceVariant = MotuSurfaceVariant,
    onSurfaceVariant = Color(0xFFA9C7D6),
    outline = Color(0xFF2B4A5E),
)

@Composable
fun MotuTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = MotuColorScheme, content = content)
}
