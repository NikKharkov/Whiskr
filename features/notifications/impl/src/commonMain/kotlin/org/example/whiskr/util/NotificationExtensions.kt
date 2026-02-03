package org.example.whiskr.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.whiskr.data.NotificationType
import org.jetbrains.compose.resources.DrawableResource
import whiskr.features.notifications.impl.generated.resources.Res
import whiskr.features.notifications.impl.generated.resources.ic_chat_
import whiskr.features.notifications.impl.generated.resources.ic_heart
import whiskr.features.notifications.impl.generated.resources.ic_notification
import whiskr.features.notifications.impl.generated.resources.ic_profile
import whiskr.features.notifications.impl.generated.resources.ic_reply
import kotlin.time.Clock
import kotlin.time.Instant

fun NotificationType.getIcon(): DrawableResource {
    return when (this) {
        NotificationType.NEW_POST -> Res.drawable.ic_notification
        NotificationType.NEW_FOLLOWER -> Res.drawable.ic_profile
        NotificationType.CHAT_MESSAGE -> Res.drawable.ic_chat_
        NotificationType.NEW_LIKE -> Res.drawable.ic_heart
        NotificationType.NEW_REPLY -> Res.drawable.ic_reply
    }
}

fun Instant.toBeautifulTime(): String {
    val timeZone = TimeZone.currentSystemDefault()

    val localDateTime = this.toLocalDateTime(timeZone)

    val now = Clock.System.now().toLocalDateTime(timeZone)

    return if (localDateTime.date == now.date) {
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        "$hour:$minute"
    } else {
        val dayOfMonth = localDateTime.day
        val month = localDateTime.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
        "$month $dayOfMonth"
    }
}