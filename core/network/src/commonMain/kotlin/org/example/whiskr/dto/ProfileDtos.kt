package org.example.whiskr.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
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