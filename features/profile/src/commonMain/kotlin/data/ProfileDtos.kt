package data

import kotlinx.serialization.Serializable
import org.example.whiskr.data.Post
import org.example.whiskr.dto.PagedResponse

@Serializable
data class Profile(
    val id: Long,
    val userId: Long,
    val handle: String,
    val displayName: String?,
    val bio: String?,
    val avatarUrl: String?,
    val followersCount: Int,
    val followingCount: Int,
    val isFollowing: Boolean,
    val isMe: Boolean
)

@Serializable
data class Pet(
    val id: Long,
    val name: String,
    val avatarUrl: String?
)

@Serializable
data class FullProfileResponseDto(
    val profile: Profile,
    val pets: List<Pet>,
    val posts: PagedResponse<Post>
)