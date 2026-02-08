package org.example.whiskr.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.example.whiskr.dto.Media

@Serializable
data class Post(
    val id: Long,
    val author: UserProfile,
    val content: String?,
    val type: PostType,
    val createdAt: LocalDateTime,
    val media: List<Media>,
    val hashtags: Set<String>,
    val stats: PostStats,
    val interaction: UserInteraction,
    val parentPost: Post? = null
)

@Serializable
data class UserProfile(
    val id: Long,
    val userId: Long,
    val displayName: String,
    val handle: String,
    val avatarUrl: String?
)

@Serializable
data class PostStats(
    val likesCount: Int,
    val repostsCount: Int,
    val repliesCount: Int
)

@Serializable
data class UserInteraction(
    val isLiked: Boolean,
    val isReposted: Boolean
)

enum class PostType { ORIGINAL, REPOST, QUOTE, REPLY }

@Serializable
data class CreatePostRequest(
    val text: String?,
    val attachedMediaUrls: List<String>? = null
)

@Serializable
data class CreateRepostRequest(
    val originalPostId: Long,
    val quoteText: String? = null
)

@Serializable
data class CreateReplyRequest(
    val targetPostId: Long,
    val text: String
)