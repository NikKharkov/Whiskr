package org.example.whiskr.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import whiskr.features.posts.api.generated.resources.Res
import whiskr.features.posts.api.generated.resources.ic_like
import whiskr.features.posts.api.generated.resources.ic_like_filled
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LikeButton(
    isLiked: Boolean,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val likeColor = WhiskrTheme.colors.like
    val animationState = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isLiked) {
        if (isLiked) {
            launch {
                animationState.snapTo(0f)
                animationState.animateTo(
                    1f,
                    animationSpec = tween(400, easing = LinearOutSlowInEasing)
                )
            }
            launch {
                scale.animateTo(0.8f, tween(50))
                scale.animateTo(1.2f, spring(dampingRatio = 0.4f))
                scale.animateTo(1f)
            }
        } else {
            animationState.snapTo(0f)
        }
    }

    Row(
        modifier = modifier.customClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLiked && animationState.value > 0f && animationState.value < 1f) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val maxRadius = size.width / 1.5f
                    val particlesCount = 8

                    for (i in 0 until particlesCount) {
                        val angle = (360f / particlesCount) * i
                        val angleRad = (angle / 180.0 * PI).toFloat()

                        val currentRadius = 10f + (maxRadius * animationState.value)

                        val x = center.x + cos(angleRad) * currentRadius
                        val y = center.y + sin(angleRad) * currentRadius

                        val radius = 3.dp.toPx() * (1f - animationState.value)

                        val color = if (i % 2 == 0) likeColor else Color(0xFFFF9800)

                        drawCircle(
                            color = color,
                            radius = radius,
                            center = Offset(x, y),
                            alpha = 1f - animationState.value
                        )
                    }
                }
            }

            Icon(
                painter = painterResource(if (isLiked) Res.drawable.ic_like_filled else Res.drawable.ic_like),
                contentDescription = null,
                tint = if (isLiked) likeColor else WhiskrTheme.colors.secondary,
                modifier = Modifier
                    .size(20.dp)
                    .scale(scale.value)
            )
        }

        Text(
            text = count.toString(),
            style = WhiskrTheme.typography.button,
            color = if (isLiked) likeColor else WhiskrTheme.colors.secondary
        )
    }
}