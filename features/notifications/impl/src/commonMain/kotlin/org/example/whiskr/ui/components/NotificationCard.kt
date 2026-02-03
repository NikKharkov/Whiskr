package org.example.whiskr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.whiskr.data.Notification
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.util.getIcon
import org.example.whiskr.util.toBeautifulTime
import org.jetbrains.compose.resources.painterResource

@Composable
fun NotificationCard(
    item: Notification,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (item.isRead) WhiskrTheme.colors.surface else WhiskrTheme.colors.primary.copy(alpha = 0.05f))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(WhiskrTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(item.type.getIcon()),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = item.title,
                style = WhiskrTheme.typography.h3,
                color = WhiskrTheme.colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (item.body.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.body,
                    style = WhiskrTheme.typography.body,
                    color = WhiskrTheme.colors.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Text(
            text = item.createdAt.toBeautifulTime(),
            style = WhiskrTheme.typography.caption,
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}