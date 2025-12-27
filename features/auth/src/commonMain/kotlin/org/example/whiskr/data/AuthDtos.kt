package org.example.whiskr.data

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequestDto(
    val email: String,
)

@Serializable
data class VerifyRequestDto(
    val email: String,
    val code: String,
)

@Serializable
data class SocialLoginRequestDto(val token: String)
