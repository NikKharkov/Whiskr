package org.example.whiskr.data

import kotlinx.serialization.Serializable

@Serializable
enum class NotificationType {
    NEW_POST,
    NEW_FOLLOWER,
    CHAT_MESSAGE
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
data class DeviceTokenRequest(val token: String)