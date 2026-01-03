package org.example.whiskr.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.Path

@Composable
fun SunMoonIcon(
    isDarkTheme: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (isDarkTheme) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "SunMoon"
    )

    Canvas(modifier = modifier.size(24.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val rotation = -45f + (360f * progress)

        rotate(rotation, center) {
            val raysScale = (1f - progress).coerceIn(0f, 1f)

            if (raysScale > 0.1f) {
                val rayCount = 8
                val rayLength = 3.dp.toPx()
                val innerRadius = 8.dp.toPx()

                for (i in 0 until rayCount) {
                    val angleRad = (2.0 * PI * i / rayCount).toFloat()

                    val start = Offset(
                        x = centerX + innerRadius * cos(angleRad),
                        y = centerY + innerRadius * sin(angleRad)
                    )
                    val end = Offset(
                        x = centerX + (innerRadius + rayLength * raysScale) * cos(angleRad),
                        y = centerY + (innerRadius + rayLength * raysScale) * sin(angleRad)
                    )

                    drawLine(
                        color = color,
                        start = start,
                        end = end,
                        strokeWidth = 2.dp.toPx() * raysScale,
                        cap = StrokeCap.Round
                    )
                }
            }

            val mainRadius = androidx.compose.ui.util.lerp(5.dp.toPx(), 10.dp.toPx(), progress)
            val eclipseOffset = androidx.compose.ui.util.lerp(mainRadius * 3, mainRadius * 0.5f, progress)

            val path = Path().apply {
                addOval(androidx.compose.ui.geometry.Rect(
                    center = center,
                    radius = mainRadius
                ))
            }

            val eclipsePath = Path().apply {
                addOval(androidx.compose.ui.geometry.Rect(
                    center = Offset(centerX - eclipseOffset, centerY - eclipseOffset),
                    radius = mainRadius
                ))
            }

            withTransform({
                clipPath(eclipsePath, clipOp = ClipOp.Difference)
            }) {
                drawPath(path, color)
            }
        }
    }
}