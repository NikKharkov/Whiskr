package org.example.whiskr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import util.LocalUser
import whiskr.flows.main.generated.resources.Res
import whiskr.flows.main.generated.resources.ic_notification
import whiskr.flows.main.generated.resources.whisp

@Composable
fun MobileTopBar(
    modifier: Modifier = Modifier,
    unreadCount: Long,
    onAvatarClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    val user = LocalUser.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AvatarPlaceholder(
            modifier = Modifier.customClickable(onClick = onAvatarClick),
            avatarUrl = user?.profile?.avatarUrl
        )

        Icon(
            painter = painterResource(Res.drawable.whisp),
            contentDescription = "Whisp",
            tint = Color.Unspecified,
            modifier = Modifier.size(48.dp)
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .customClickable(onClick = onNotificationsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_notification),
                contentDescription = null,
                tint = WhiskrTheme.colors.onBackground,
                modifier = Modifier.size(24.dp)
            )

            if (unreadCount > 0) {
                val badgeText = if (unreadCount >= 10) "9+" else unreadCount.toString()

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp)
                        .background(WhiskrTheme.colors.error, CircleShape)
                        .defaultMinSize(minWidth = 16.dp, minHeight = 16.dp)
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeText,
                        style = WhiskrTheme.typography.caption,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}