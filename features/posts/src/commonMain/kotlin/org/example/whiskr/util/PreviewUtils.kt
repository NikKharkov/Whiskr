package org.example.whiskr.util

import data.UserResponseDto
import domain.UserState
import kotlinx.datetime.LocalDateTime
import org.example.whiskr.dto.*

val mockUserDto = UserResponseDto(
    id = 1L, userId = 101L, handle = "current_user", displayName = "Me",
    bio = "Bio", avatarUrl = null, followersCount = 0
)
val mockUserState = UserState(profile = mockUserDto)

val mockAuthorProfile = UserProfile(
    id = 1L,
    userId = 55L,
    displayName = "Kotik Boris",
    handle = "boris_the_cat",
    avatarUrl = null
)
val mockStats = PostStats(
    likesCount = 128,
    repostsCount = 12,
    repliesCount = 5
)

val mockInteraction = UserInteraction(
    isLiked = true,
    isReposted = false
)

val mockMediaList = listOf(
    PostMedia(
        id = 99L,
        url = "https://example.com/image.jpg",
        type = MediaType.IMAGE,
        width = 1080,
        height = 1080
    )
)
val mockPost = Post(
    id = 777L,
    author = mockAuthorProfile,
    content = "This mock data is now strictly typed! Checking nested stats and interactions.",
    type = PostType.ORIGINAL,
    createdAt = LocalDateTime(2026, 1, 26, 14, 30, 0, 0),
    media = mockMediaList,
    hashtags = setOf("kotlin", "compose"),
    stats = mockStats,
    interaction = mockInteraction,
    parentPost = null
)

val mockFeed = List(5) { index ->
    mockPost.copy(id = index.toLong(), content = "Post #$index in the feed")
}

val mockPostForReply = Post(
    id = 1L,
    author = UserProfile(1L, 101L, "Mock User", "mock_user", null),
    content = "Target post content",
    type = PostType.ORIGINAL,
    createdAt = LocalDateTime(2026, 1, 1, 12, 0),
    media = emptyList(), hashtags = emptySet(),
    stats = PostStats(0,0,0), interaction = UserInteraction(false, false)
)