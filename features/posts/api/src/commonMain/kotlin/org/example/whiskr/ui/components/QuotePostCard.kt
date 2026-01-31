package org.example.whiskr.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.util.rememberRelativeTime

@Composable
fun QuotePostCard(
    post: Post,
    onMediaClick: (List<PostMedia>, Int) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onRepostClick: () -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: (String) -> Unit,
    onNavigateToOriginal: (Post) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedTime = rememberRelativeTime(post.createdAt)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AvatarPlaceholder(
                avatarUrl = post.author.avatarUrl,
                modifier = Modifier.customClickable(onClick = { onProfileClick(post.author.handle) })
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .customClickable(onClick = onCommentClick),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PostHeader(
                    displayName = post.author.displayName,
                    handle = post.author.handle,
                    formattedTime = formattedTime
                )

                if (post.content != null) {
                    Text(
                        text = post.content,
                        style = WhiskrTheme.typography.body,
                        color = WhiskrTheme.colors.onBackground
                    )
                }

                if (post.media.isNotEmpty()) {
                    PostMediaCarousel(
                        medias = post.media,
                        onMediaClick = { index -> onMediaClick(post.media, index) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                post.parentPost?.let { original ->
                    EmbeddedPostCard(
                        post = original,
                        onMediaClick = { index -> onMediaClick(original.media, index) },
                        onClick = { onNavigateToOriginal(original) },
                        onProfileClick = { onProfileClick(original.author.handle) }
                    )
                }

                PostActionsRow(
                    stats = post.stats,
                    interaction = post.interaction,
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick,
                    onRepostClick = onRepostClick,
                    onShareClick = onShareClick
                )
            }
        }
    }
}


@Composable
private fun EmbeddedPostCard(
    post: Post,
    onClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMediaClick: (Int) -> Unit
) {
    val formattedTime = rememberRelativeTime(post.createdAt)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, WhiskrTheme.colors.outline, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .customClickable(onClick = onClick)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarPlaceholder(
                avatarUrl = post.author.avatarUrl,
                modifier = Modifier.size(20.dp).customClickable(onClick = onProfileClick)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = post.author.displayName,
                style = WhiskrTheme.typography.button,
                color = WhiskrTheme.colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = " · $formattedTime",
                style = WhiskrTheme.typography.caption,
                color = WhiskrTheme.colors.secondary
            )
        }

        if (post.content != null) {
            Text(
                text = post.content,
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.onBackground,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (post.media.isNotEmpty()) {
            PostMediaCarousel(
                medias = post.media,
                onMediaClick = { index ->
                    onMediaClick(index)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}