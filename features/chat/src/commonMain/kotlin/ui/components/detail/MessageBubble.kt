package ui.components.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.ChatMessageDto
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import util.toMessageTime
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.ic_check_read

@Composable
fun MessageBubble(
    message: ChatMessageDto,
    isMe: Boolean,
    onMediaClick: (Int) -> Unit
) {
    val textColor = if (isMe) Color.White else WhiskrTheme.colors.onBackground
    val timeColor = textColor.copy(alpha = 0.75f)
    val hasAttachments = message.attachments.isNotEmpty()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .widthIn(min = 80.dp, max = 320.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .background(if (isMe) WhiskrTheme.colors.primary else WhiskrTheme.colors.surface)
        ) {
            if (hasAttachments) {
                ChatMediaCarousel(
                    attachments = message.attachments,
                    onMediaClick = onMediaClick,
                    modifier = Modifier.padding(if (hasAttachments && message.content.isNullOrBlank()) 0.dp else 2.dp)
                )
            }

            if (!message.content.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .then(if (hasAttachments) Modifier.fillMaxWidth() else Modifier)
                ) {
                    Text(
                        text = message.content,
                        style = WhiskrTheme.typography.body,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = message.createdAt.toMessageTime(),
                            style = WhiskrTheme.typography.caption.copy(fontSize = 10.sp),
                            color = timeColor
                        )

                        if (isMe) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_check_read),
                                contentDescription = null,
                                tint = if (message.isRead) Color.White else Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            } else if (hasAttachments) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 12.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .offset(y = (-12).dp, x = (-4).dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = message.createdAt.toMessageTime(),
                            style = WhiskrTheme.typography.caption.copy(fontSize = 10.sp),
                            color = Color.White
                        )

                        if (isMe) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_check_read),
                                contentDescription = null,
                                tint = if (message.isRead) Color.White else Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}