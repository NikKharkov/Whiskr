package org.example.whiskr.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: UserProfile,
    val content: String?,
    val type: PostType,
    val createdAt: LocalDateTime,
    val media: List<PostMedia>,
    val stats: PostStats,
    val interaction: UserInteraction,
    val parentPost: Post? = null
)

@Serializable
data class PostMedia(
    val id: Long,
    val url: String,
    val type: MediaType,
    val width: Int,
    val height: Int,
    val thumbnailUrl: String? = null,
    val duration: Int? = null
) {
    val aspectRatio: Float
        get() {
            if (height == 0) return 1f
            val rawRatio = width.toFloat() / height.toFloat()
            return rawRatio.coerceIn(0.8f, 1.91f)
        }
}

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
data class CreatePostRequest(
    val text: String?,
    val durationSeconds: Int?
)

@Serializable
data class CreateRepostRequest(
    val originalPostId: Long,
    val quoteText: String? = null
)

@Serializable
data class UserInteraction(
    val isLiked: Boolean,
    val isReposted: Boolean
)

enum class PostType { ORIGINAL, REPOST, QUOTE, REPLY }
enum class MediaType { IMAGE, VIDEO }