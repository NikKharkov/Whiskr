package org.example.whiskr.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.launch
import org.example.whiskr.extensions.circularClip
import org.example.whiskr.extensions.getDistanceToCorner

@Composable
fun ThemeRevealContainer(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    content: @Composable (isDarkTheme: Boolean, onReveal: (Offset) -> Unit) -> Unit
) {
    var containerSize by remember { mutableStateOf(Size.Zero) }
    var revealOffset by remember { mutableStateOf(Offset.Zero) }

    var isAnimating by remember { mutableStateOf(false) }
    var previousThemeIsDark by remember { mutableStateOf(isDarkTheme) }

    val radiusAnim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { containerSize = it.size.toSize() }
    ) {
        if (isAnimating) {
            key(previousThemeIsDark) {
                content(previousThemeIsDark) {}
            }

            key(!previousThemeIsDark) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .circularClip(radiusAnim.value, revealOffset)
                ) {
                    content(!previousThemeIsDark) {}
                }
            }
        } else {
            content(isDarkTheme) { clickOffset ->
                revealOffset = clickOffset
                previousThemeIsDark = isDarkTheme
                isAnimating = true

                scope.launch {
                    val maxRadius = containerSize.getDistanceToCorner(revealOffset)
                    radiusAnim.snapTo(0f)

                    radiusAnim.animateTo(
                        targetValue = maxRadius,
                        animationSpec = tween(700, easing = FastOutSlowInEasing)
                    )

                    onToggleTheme(!isDarkTheme)
                    isAnimating = false
                }
            }
        }
    }
}