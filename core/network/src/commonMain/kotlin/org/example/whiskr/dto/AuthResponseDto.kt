package org.example.whiskr.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String
)
