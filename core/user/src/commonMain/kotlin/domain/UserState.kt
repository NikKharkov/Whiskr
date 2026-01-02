package domain

import data.UserResponseDto

data class UserState(
    val profile: UserResponseDto? = null,
    val isLoading: Boolean = false
)
