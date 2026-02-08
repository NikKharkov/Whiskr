package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.data.Post
import org.example.whiskr.dto.Media
import org.example.whiskr.data.PostType
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.PostActionsRow
import org.example.whiskr.ui.components.PostHeader
import org.example.whiskr.ui.components.PostMediaCarousel
import org.example.whiskr.ui.components.QuotePostCard
import org.example.whiskr.ui.components.RepostLabel
import org.example.whiskr.util.rememberRelativeTime

@Composable
fun PostCard(
    post: Post,
    onMediaClick: (List<Media>, Int) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onRepostClick: () -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: (String) -> Unit,
    onHashtagClick: (String) -> Unit,
    onNavigateToOriginal: (Post) -> Unit,
    modifier: Modifier = Modifier
) {
    when (post.type) {
        PostType.QUOTE -> {
            QuotePostCard(
                post = post,
                onMediaClick = onMediaClick,
                onLikeClick = onLikeClick,
                onCommentClick = onCommentClick,
                onRepostClick = onRepostClick,
                onShareClick = onShareClick,
                onProfileClick = onProfileClick,
                onNavigateToOriginal = onNavigateToOriginal,
                modifier = modifier
            )
        }

        PostType.REPOST -> {
            Column(modifier = modifier) {
                RepostLabel(
                    reposterName = post.author.displayName,
                    modifier = Modifier.padding(start = 48.dp, top = 8.dp, bottom = 4.dp)
                )

                RegularPostContent(
                    post = post.parentPost ?: post,
                    onMediaClick = onMediaClick,
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick,
                    onRepostClick = onRepostClick,
                    onShareClick = onShareClick,
                    onProfileClick = onProfileClick,
                    onHashtagClick = onHashtagClick
                )
            }
        }

        else -> {
            RegularPostContent(
                post = post,
                onMediaClick = onMediaClick,
                onLikeClick = onLikeClick,
                onCommentClick = onCommentClick,
                onRepostClick = onRepostClick,
                onShareClick = onShareClick,
                onProfileClick = onProfileClick,
                onHashtagClick = onHashtagClick,
                modifier = modifier
            )
        }
    }
}


@Composable
private fun RegularPostContent(
    post: Post,
    onMediaClick: (List<Media>, Int) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onRepostClick: () -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: (String) -> Unit,
    onHashtagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedTime = rememberRelativeTime(post.createdAt)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .customClickable(onClick = onCommentClick)
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AvatarPlaceholder(
                avatarUrl = post.author.avatarUrl,
                modifier = Modifier.customClickable(onClick = { onProfileClick(post.author.handle) })
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

                if (post.hashtags.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        post.hashtags.forEach { tag ->
                            Text(
                                text = "#$tag",
                                style = WhiskrTheme.typography.body,
                                color = WhiskrTheme.colors.primary,
                                modifier = Modifier.customClickable(onClick = { onHashtagClick(tag) })
                            )
                        }
                    }
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