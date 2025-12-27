package org.example.whiskr.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDto(
    val token: String
)
