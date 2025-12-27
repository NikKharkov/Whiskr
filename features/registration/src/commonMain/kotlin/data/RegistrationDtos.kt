package data

import kotlinx.serialization.Serializable

@Serializable
data class SetupProfileRequest(
    val handle: String,
    val displayName: String
)