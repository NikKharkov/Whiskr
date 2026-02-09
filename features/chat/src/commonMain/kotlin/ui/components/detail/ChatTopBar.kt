package ui.components.detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.detail.ChatDetailComponent
import data.UserState
import kotlinx.coroutines.delay
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import util.toLastSeenText
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.ic_arrow_back
import whiskr.features.chat.generated.resources.online
import whiskr.features.chat.generated.resources.typing

@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    model: ChatDetailComponent.Model,
    onBackClick: () -> Unit,
    onProfileClick: (String) -> Unit
) {
    val chatInfo = model.chatInfo
    val isTyping = model.typingUserIds.isNotEmpty()
    val isOnline = model.partnerStatus == UserState.ONLINE

    val statusText = when {
        isTyping -> stringResource(Res.string.typing)
        isOnline -> stringResource(Res.string.online)
        else -> chatInfo?.lastActivity?.toLastSeenText() ?: ""
    }

    val statusColor = when {
        isTyping -> WhiskrTheme.colors.primary
        isOnline -> WhiskrTheme.colors.primary
        else -> WhiskrTheme.colors.secondary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(WhiskrTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null,
                tint = WhiskrTheme.colors.onBackground
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .customClickable {
                    chatInfo?.partnerHandle?.let(onProfileClick)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarPlaceholder(
                avatarUrl = chatInfo?.partnerAvatar,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = chatInfo?.partnerName ?: "Chat",
                    style = WhiskrTheme.typography.h3.copy(fontSize = 16.sp),
                    color = WhiskrTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = statusText,
                        style = WhiskrTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                        color = statusColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (isTyping) {
                        Spacer(modifier = Modifier.width(4.dp))

                        TypingDotsIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun TypingDotsIndicator() {

    val distance = with(LocalDensity.current) { 3.dp.toPx() }

    val dots = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )

    dots.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 using LinearEasing
                        1.0f at 300 using LinearEasing
                        0.0f at 600 using LinearEasing
                        0.0f at 1200 using LinearEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        dots.forEach { animatable ->
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .offset(y = (-animatable.value * distance).dp)
                    .background(color = WhiskrTheme.colors.primary, shape = CircleShape)
            )
        }
    }
}