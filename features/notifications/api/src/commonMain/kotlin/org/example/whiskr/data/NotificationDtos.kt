package org.example.whiskr.data

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
enum class NotificationType {
    NEW_POST,
    NEW_FOLLOWER,
    CHAT_MESSAGE,
    NEW_LIKE,
    NEW_REPLY
}

@Serializable
data class PushPayload(
    val title: String,
    val body: String,
    val type: NotificationType,
    val targetId: String,
    val deepLink: String
)

@Serializable
data class Notification(
    val id: Long,
    val title: String,
    val body: String,
    val type: NotificationType,
    val targetId: String?,
    val deepLink: String?,
    val isRead: Boolean,
    val createdAt: Instant
)

@Serializable
data class DeviceTokenRequest(val token: String)