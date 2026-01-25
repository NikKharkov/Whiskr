package org.example.whiskr.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.sin
import kotlin.random.Random

private data class ConfettiParticle(
    var x: Float,
    var y: Float,
    var vy: Float,
    var swayOffset: Float,
    var swaySpeed: Float,
    var alpha: Float = 1f,
    val color: Color,
    val size: Float,
    var rotation: Float,
    val rotationSpeed: Float
)

@Composable
fun ConfettiOverlay(
    modifier: Modifier = Modifier,
    trigger: Long,
    colors: List<Color>
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current

        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        val particles = remember { mutableStateListOf<ConfettiParticle>() }
        var lastFrameTime by remember { mutableStateOf(0L) }

        LaunchedEffect(trigger) {
            if (trigger == 0L) return@LaunchedEffect
            lastFrameTime = 0L
            particles.clear()

            repeat(400) {
                particles.add(
                    ConfettiParticle(
                        x = Random.nextFloat() * widthPx,
                        y = -Random.nextFloat() * heightPx * 1.5f - 50f,
                        vy = Random.nextFloat() * 10f + 5f,
                        swayOffset = Random.nextFloat() * 100f,
                        swaySpeed = Random.nextFloat() * 0.1f + 0.05f,
                        color = colors.random(),
                        size = Random.nextFloat() * 20f + 10f,
                        rotation = Random.nextFloat() * 360f,
                        rotationSpeed = Random.nextFloat() * 5f - 2.5f
                    )
                )
            }

            while (particles.isNotEmpty()) {
                withFrameNanos { currentFrameTime ->
                    if (lastFrameTime == 0L) lastFrameTime = currentFrameTime
                    val delta = (currentFrameTime - lastFrameTime) / 1_000_000f
                    lastFrameTime = currentFrameTime

                    val dt = delta / 16f

                    val iterator = particles.listIterator()
                    while (iterator.hasNext()) {
                        val p = iterator.next()

                        p.y += p.vy * dt
                        p.vy += 0.2f * dt

                        p.x += sin(p.y * 0.01f + p.swayOffset) * 2f * dt

                        p.rotation += p.rotationSpeed * dt

                        if (p.y > heightPx * 0.8f) {
                            p.alpha -= 0.02f * dt
                        }

                        if (p.alpha <= 0f || p.y > heightPx + 100f) {
                            iterator.remove()
                        }
                    }
                }
            }
        }

        if (particles.isNotEmpty()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                particles.forEach { p ->
                    withTransform({
                        translate(left = p.x, top = p.y)
                        rotate(degrees = p.rotation, pivot = Offset.Zero)
                    }) {
                        drawRect(
                            color = p.color.copy(alpha = p.alpha.coerceIn(0f, 1f)),
                            topLeft = Offset(-p.size / 2, -p.size / 2),
                            size = Size(p.size, p.size)
                        )
                    }
                }
            }
        }
    }
}