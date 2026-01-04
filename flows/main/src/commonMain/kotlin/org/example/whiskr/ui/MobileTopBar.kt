package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import whiskr.flows.main.generated.resources.Res
import whiskr.flows.main.generated.resources.ic_notification
import whiskr.flows.main.generated.resources.whisp

@Composable
fun MobileTopBar(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    isThereUnreadNotifications: Boolean,
    onAvatarClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AvatarPlaceholder(
            modifier = Modifier.customClickable(onClick = onAvatarClick),
            avatarUrl = avatarUrl
        )

        Icon(
            painter = painterResource(Res.drawable.whisp),
            contentDescription = "Whisp",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Box(
            modifier = Modifier
                .size(24.dp)
                .customClickable(onClick = onNotificationsClick)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_notification),
                contentDescription = null,
                tint = WhiskrTheme.colors.onBackground,
                modifier = Modifier.fillMaxSize()
            )

            if (isThereUnreadNotifications) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(WhiskrTheme.colors.error)
                        .align(Alignment.TopEnd)
                        .offset(4.dp, 4.dp)
                )
            }
        }
    }
}