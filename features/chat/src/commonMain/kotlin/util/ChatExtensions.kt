package util

import androidx.compose.runtime.Composable
import data.ChatMessageDto
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.last_seen_at
import whiskr.features.chat.generated.resources.last_seen_date
import whiskr.features.chat.generated.resources.last_seen_yesterday
import whiskr.features.chat.generated.resources.media_photo
import whiskr.features.chat.generated.resources.media_video
import whiskr.features.chat.generated.resources.no_messages
import whiskr.features.chat.generated.resources.type_message
import whiskr.features.chat.generated.resources.you_prefix
import kotlin.time.Clock
import kotlin.time.Instant

fun Instant.toMessageTime(): String {
    val local = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = local.hour.toString().padStart(2, '0')
    val minute = local.minute.toString().padStart(2, '0')
    return "$hour:$minute"
}

@Composable
fun Instant.toLastSeenText(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val now = Clock.System.now().toLocalDateTime(timeZone)
    val messageTime = this.toLocalDateTime(timeZone)

    val timeStr = "${messageTime.hour.toString().padStart(2, '0')}:${messageTime.minute.toString().padStart(2, '0')}"
    val dateStr = "${messageTime.day.toString().padStart(2, '0')}.${messageTime.month.number.toString().padStart(2, '0')}.${messageTime.year}"

    val today = now.date
    val yesterday = today.minus(1, DateTimeUnit.DAY)

    return when (messageTime.date) {
        today -> stringResource(Res.string.last_seen_at, timeStr)
        yesterday -> stringResource(Res.string.last_seen_yesterday, timeStr)
        else -> stringResource(Res.string.last_seen_date, dateStr)
    }
}

@Composable
fun getMessagePreviewText(message: ChatMessageDto?, currentUserId: Long): String {
    if (message == null) return stringResource(Res.string.no_messages)

    val content = when {
        !message.content.isNullOrBlank() -> message.content
        message.attachments.any { it.type == "IMAGE" } -> stringResource(Res.string.media_photo)
        message.attachments.any { it.type == "VIDEO" } -> stringResource(Res.string.media_video)
        else -> stringResource(Res.string.type_message)
    }

    return if (message.senderId == currentUserId) {
        stringResource(Res.string.you_prefix, content)
    } else {
        content
    }
}