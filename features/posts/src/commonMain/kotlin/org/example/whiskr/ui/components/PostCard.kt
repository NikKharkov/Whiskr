package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.dto.Post
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.card.PostActionsRow
import org.example.whiskr.ui.components.card.PostMediaCarousel
import org.example.whiskr.util.rememberRelativeTime

@Composable
fun PostCard(
    post: Post,
    onPostClick: (Int) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onRepostClick: () -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHashtagClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedTime = rememberRelativeTime(post.createdAt)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            AvatarPlaceholder(
                avatarUrl = post.author.avatarUrl,
                modifier = Modifier.customClickable(onClick = onProfileClick)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = post.author.displayName,
                        style = WhiskrTheme.typography.h3,
                        color = WhiskrTheme.colors.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${post.author.handle} · $formattedTime",
                        style = WhiskrTheme.typography.body,
                        color = WhiskrTheme.colors.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (post.content != null) {
                    Text(
                        text = post.content!!,
                        style = WhiskrTheme.typography.body,
                        color = WhiskrTheme.colors.onBackground
                    )
                }

                if (post.media.isNotEmpty()) {
                    PostMediaCarousel(
                        medias = post.media,
                        onMediaClick = { index ->
                            onPostClick(index)
                        },
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