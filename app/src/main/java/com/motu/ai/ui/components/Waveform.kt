package com.motu.ai.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun Waveform(active: Boolean, modifier: Modifier = Modifier, bars: Int = 28) {
    val color = MaterialTheme.colorScheme.primary

    val phaseState: State<Float>? = if (active) {
        val transition = rememberInfiniteTransition(label = "wave")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * PI).toFloat(),
            animationSpec = infiniteRepeatable(tween(durationMillis = 1400, easing = LinearEasing)),
            label = "phase",
        )
    } else {
        null
    }

    Canvas(modifier = modifier) {
        val phase = phaseState?.value ?: 0f
        val slot = size.width / bars
        val cy = size.height / 2f
        for (i in 0 until bars) {
            val h = if (active) 0.2f + 0.8f * abs(sin(phase + i * 0.55f)) else 0.1f
            val x = slot * (i + 0.5f)
            val half = size.height * h / 2f
            drawLine(
                color = color.copy(alpha = 0.4f + 0.6f * h),
                start = Offset(x, cy - half),
                end = Offset(x, cy + half),
                strokeWidth = slot * 0.5f,
                cap = StrokeCap.Round,
            )
        }
    }
}
