package data

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
enum class ChatType {
    PRIVATE,
    GROUP
}

@Serializable
enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    SYSTEM
}

@Serializable
enum class UserState {
    ONLINE, OFFLINE, TYPING, STOP_TYPING, READ
}

@Serializable
data class ChatStateEvent(
    val chatId: Long,
    val userId: Long,
    val state: UserState
)

@Serializable
data class SendMessageRequest(
    val content: String?,
    val attachmentUrls: List<String>? = null,
    val type: MessageType = MessageType.TEXT,
    val replyToId: Long? = null
)

@Serializable
data class ChatMessageDto(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val senderName: String,
    val senderAvatar: String?,
    val content: String?,
    val type: MessageType,
    val attachments: List<ChatAttachmentDto> = emptyList(),
    val replyToId: Long?,
    val createdAt: Instant,
    val isRead: Boolean
)
@Serializable
data class ChatDto(
    val id: Long,
    val type: ChatType,
    val groupTitle: String?,
    val groupAvatarUrl: String?,
    val partnerId: Long?,
    val partnerName: String?,
    val partnerHandle: String?,
    val partnerAvatar: String?,
    val lastMessage: ChatMessageDto?,
    val unreadCount: Long,
    val isOnline: Boolean = false,
    val lastActivity: Instant
)

@Serializable
data class ChatAttachmentDto(
    val url: String,
    val type: String
)