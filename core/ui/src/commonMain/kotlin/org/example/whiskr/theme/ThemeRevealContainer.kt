package org.example.whiskr.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.max

@Composable
fun ThemeRevealContainer(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    content: @Composable (startAnimation: (Offset) -> Unit) -> Unit
) {
    val capturer = rememberScreenCapturer()
    val scope = rememberCoroutineScope()

    var oldThemeScreenshot by remember { mutableStateOf<ImageBitmap?>(null) }
    var revealOffset by remember { mutableStateOf(Offset.Zero) }
    var isAnimating by remember { mutableStateOf(false) }

    val radiusAnim = remember { Animatable(0f) }

    val sharedContent = remember {
        movableContentOf { trigger: (Offset) -> Unit ->
            content(trigger)
        }
    }

    val startAnimation: (Offset) -> Unit = { offset ->
        if (!isAnimating) {
            isAnimating = true
            revealOffset = offset

            scope.launch {
                val shot = capturer.capture()

                if (shot != null) {
                    oldThemeScreenshot = shot

                    onToggleTheme(!isDarkTheme)

                    val maxRadius = hypot(
                        max(revealOffset.x, shot.width.toFloat()),
                        max(revealOffset.y, shot.height.toFloat())
                    ) * 1.1f

                    radiusAnim.snapTo(0f)
                    radiusAnim.animateTo(
                        targetValue = maxRadius,
                        animationSpec = tween(700, easing = FastOutSlowInEasing)
                    )

                    oldThemeScreenshot = null
                    isAnimating = false
                } else {
                    onToggleTheme(!isDarkTheme)
                    isAnimating = false
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isAnimating && oldThemeScreenshot != null) {
            Image(
                bitmap = oldThemeScreenshot!!,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        clip = true
                        shape = object : Shape {
                            override fun createOutline(
                                size: Size,
                                layoutDirection: LayoutDirection,
                                density: Density
                            ): Outline {
                                val path = Path().apply {
                                    addOval(Rect(revealOffset, radiusAnim.value))
                                }
                                return Outline.Generic(path)
                            }
                        }
                    }
            ) {
                sharedContent(startAnimation)
            }
        } else {
            sharedContent(startAnimation)
        }
    }
}