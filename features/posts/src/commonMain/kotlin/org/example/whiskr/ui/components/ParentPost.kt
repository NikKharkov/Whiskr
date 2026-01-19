package org.example.whiskr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.dto.Post
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.card.LikeButton
import org.example.whiskr.ui.components.card.PostMediaCarousel
import org.example.whiskr.util.rememberRelativeTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.ic_comments
import whiskr.features.posts.generated.resources.ic_repost
import whiskr.features.posts.generated.resources.ic_share
import whiskr.features.posts.generated.resources.likes
import whiskr.features.posts.generated.resources.reposts

@Composable
fun ParentPost(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentsClick: () -> Unit,
    onMediaClick: (Int) -> Unit,
    onRepostClick: () -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiskrTheme.colors.background)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AvatarPlaceholder(
                avatarUrl = post.author.avatarUrl,
                modifier = Modifier
                    .size(48.dp)
                    .customClickable(onClick = onProfileClick)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = post.author.displayName,
                    style = WhiskrTheme.typography.h3,
                    color = WhiskrTheme.colors.onBackground
                )
                Text(
                    text = post.author.handle,
                    style = WhiskrTheme.typography.body,
                    color = WhiskrTheme.colors.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (post.content != null) {
            Text(
                text = post.content!!,
                style = WhiskrTheme.typography.body.copy(fontSize = 18.sp, lineHeight = 26.sp),
                color = WhiskrTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (post.media.isNotEmpty()) {
            PostMediaCarousel(
                medias = post.media,
                onMediaClick = { index ->
                    onMediaClick(index)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .widthIn(max = 400.dp)
                    .heightIn(max = 400.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = rememberRelativeTime(post.createdAt),
            style = WhiskrTheme.typography.caption.copy(fontSize = 14.sp),
            color = WhiskrTheme.colors.secondary
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = WhiskrTheme.colors.outline.copy(alpha = 0.5f)
        )

        if (post.stats.repostsCount > 0 || post.stats.likesCount > 0) {

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (post.stats.repostsCount > 0) {
                    StatText(
                        count = post.stats.repostsCount,
                        label = stringResource(Res.string.reposts)
                    )
                }
                if (post.stats.likesCount > 0) {
                    StatText(
                        count = post.stats.likesCount,
                        label = stringResource(Res.string.likes)
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = WhiskrTheme.colors.outline.copy(alpha = 0.5f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_comments),
                contentDescription = null,
                tint = WhiskrTheme.colors.secondary,
                modifier = Modifier
                    .size(24.dp)
                    .customClickable(onClick = onCommentsClick)
            )

            Icon(
                painter = painterResource(Res.drawable.ic_repost),
                contentDescription = null,
                tint = WhiskrTheme.colors.secondary,
                modifier = Modifier
                    .size(24.dp)
                    .customClickable(onClick = onRepostClick)
            )

            LikeButton(
                isLiked = post.interaction.isLiked,
                count = post.stats.likesCount,
                onClick = onLikeClick
            )

            Icon(
                painter = painterResource(Res.drawable.ic_share),
                contentDescription = null,
                tint = WhiskrTheme.colors.secondary,
                modifier = Modifier
                    .size(24.dp)
                    .customClickable(onClick = onShareClick)
            )
        }
    }
}

@Composable
private fun StatText(count: Int, label: String) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = count.toString(),
            style = WhiskrTheme.typography.h3.copy(fontSize = 16.sp),
            color = WhiskrTheme.colors.onBackground
        )

        Text(
            text = label,
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.secondary
        )
    }
}