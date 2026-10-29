package com.motu.ai.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

@Composable
fun AiCore(active: Boolean, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary

    val transition = rememberInfiniteTransition(label = "core")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (active) 5000 else 14000, easing = LinearEasing),
        ),
        label = "rotation",
    )
    val pulse by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (active) 800 else 2600,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Canvas(modifier = modifier.aspectRatio(1f)) {
        val c = center
        val r = size.minDimension / 2f

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primary.copy(alpha = if (active) 0.45f else 0.25f), Color.Transparent),
                center = c,
                radius = r,
            ),
            radius = r,
        )

        drawCircle(
            color = primary.copy(alpha = 0.35f),
            radius = r * 0.85f,
            style = Stroke(width = r * 0.008f),
        )

        rotate(degrees = rotation, pivot = c) {
            arcRing(primary, r * 0.7f, startAngle = 0f, sweep = 100f, strokeWidth = r * 0.035f)
            arcRing(primary, r * 0.7f, startAngle = 180f, sweep = 60f, strokeWidth = r * 0.035f)
        }
        rotate(degrees = -rotation * 1.5f, pivot = c) {
            arcRing(secondary, r * 0.55f, startAngle = 40f, sweep = 120f, strokeWidth = r * 0.03f)
            arcRing(secondary, r * 0.55f, startAngle = 220f, sweep = 80f, strokeWidth = r * 0.03f)
        }

        val coreRadius = r * 0.3f * pulse
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, primary, secondary, Color.Transparent),
                center = c,
                radius = coreRadius,
            ),
            radius = coreRadius,
        )
    }
}

private fun DrawScope.arcRing(
    color: Color,
    radius: Float,
    startAngle: Float,
    sweep: Float,
    strokeWidth: Float,
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2f, radius * 2f),
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )
}
