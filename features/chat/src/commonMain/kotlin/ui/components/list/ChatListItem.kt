package ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.ChatDto
import data.ChatType
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import util.getMessagePreviewText
import util.toMessageTime
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.ic_check_read
import whiskr.features.chat.generated.resources.unknown_user

@Composable
fun ChatListItem(
    chat: ChatDto,
    currentUserId: Long,
    onClick: () -> Unit
) {
    val title = if (chat.type == ChatType.GROUP) chat.groupTitle else chat.partnerName
    val avatar = if (chat.type == ChatType.GROUP) chat.groupAvatarUrl else chat.partnerAvatar

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AvatarPlaceholder(avatarUrl = avatar)

            if (chat.type == ChatType.PRIVATE && chat.isOnline) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .border(2.dp, WhiskrTheme.colors.background, CircleShape)
                        .background(Color.Green, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title ?: stringResource(Res.string.unknown_user),
                    style = WhiskrTheme.typography.h3.copy(fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = WhiskrTheme.colors.onBackground,
                    modifier = Modifier.weight(1f)
                )

                chat.lastMessage?.createdAt?.let {
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = it.toMessageTime(),
                        style = WhiskrTheme.typography.caption,
                        color = if (chat.unreadCount > 0) WhiskrTheme.colors.primary else WhiskrTheme.colors.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getMessagePreviewText(chat.lastMessage, currentUserId),
                    style = WhiskrTheme.typography.body.copy(fontSize = 14.sp),
                    color = if (chat.unreadCount > 0) WhiskrTheme.colors.onBackground else WhiskrTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.unreadCount > 0) {

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .background(WhiskrTheme.colors.primary, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                            style = WhiskrTheme.typography.caption.copy(fontSize = 10.sp),
                            color = Color.White
                        )
                    }
                } else if (chat.lastMessage?.senderId == currentUserId) {

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        painter = painterResource(Res.drawable.ic_check_read),
                        contentDescription = null,
                        tint = if (chat.lastMessage.isRead) WhiskrTheme.colors.primary else WhiskrTheme.colors.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}