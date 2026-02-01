package data

import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileRequest(
    val handle: String,
    val displayName: String
)