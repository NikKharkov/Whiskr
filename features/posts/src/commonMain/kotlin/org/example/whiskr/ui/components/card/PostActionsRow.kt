package org.example.whiskr.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.example.whiskr.dto.PostStats
import org.example.whiskr.dto.UserInteraction
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.ic_comments
import whiskr.features.posts.generated.resources.ic_repost
import whiskr.features.posts.generated.resources.ic_share

@Composable
fun PostActionsRow(
    stats: PostStats,
    interaction: UserInteraction,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onRepostClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PostActionButton(
            icon = painterResource(Res.drawable.ic_comments),
            count = stats.repliesCount,
            onClick = onCommentClick
        )

        PostActionButton(
            icon = painterResource(Res.drawable.ic_repost),
            count = stats.repostsCount,
            onClick = onRepostClick
        )

        LikeButton(
            isLiked = interaction.isLiked,
            count = stats.likesCount,
            onClick = onLikeClick
        )

        Icon(
            painter = painterResource(Res.drawable.ic_share),
            contentDescription = null,
            tint = WhiskrTheme.colors.secondary,
            modifier = Modifier
                .size(20.dp)
                .customClickable(onClick = onShareClick)
        )
    }
}

@Composable
private fun PostActionButton(
    icon: Painter,
    count: Int,
    onClick: () -> Unit,
    tint: Color = WhiskrTheme.colors.secondary
) {
    Row(
        modifier = Modifier.customClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = count.toString(),
            style = WhiskrTheme.typography.button,
            color = tint
        )
    }
}