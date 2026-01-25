package org.example.whiskr.extensions

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode


fun Modifier.animatedGradientBackground(
    colors: List<Color>,
    animationTimeMillis: Int = 10000
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "gradientTransition")

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationTimeMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientOffset"
    )

    drawBehind {
        val width = size.width
        val height = size.height

        val brush = Brush.linearGradient(
            colors = colors,
            start = Offset(0f - (width * translateAnimation), 0f),
            end = Offset(width + (width * translateAnimation), height),
            tileMode = TileMode.Mirror
        )

        drawRect(brush = brush)
    }
}