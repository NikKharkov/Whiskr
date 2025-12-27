package data

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    val id: Long,
    val userId: Long,
    val handle: String,
    val displayName: String?,
    val bio: String?,
    val avatarUrl: String?,
    val followersCount: Int
)