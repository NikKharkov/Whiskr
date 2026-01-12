package org.example.whiskr.ui.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.data.Post
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.util.rememberRelativeTime
import util.LocalUser

@Composable
fun PostCard(
    post: Post,
    onPostClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onRepostClick: () -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user = LocalUser.current
    val formattedTime = rememberRelativeTime(post.createdAt)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 550.dp)
            .clickable(onClick = onPostClick)
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            AvatarPlaceholder(
                avatarUrl = user?.profile?.avatarUrl,
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
                        text = post.content,
                        style = WhiskrTheme.typography.body,
                        color = WhiskrTheme.colors.onBackground
                    )
                }

                if (post.media.isNotEmpty()) {
                    PostMediaCarousel(
                        medias = post.media
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