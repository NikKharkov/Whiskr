package org.example.whiskr.dto

import kotlinx.serialization.Serializable

@Serializable
data class Media(
    val id: Long,
    val url: String,
    val type: MediaType,
    val width: Int,
    val height: Int,
    val thumbnailUrl: String? = null,
    val duration: Int? = null
)

enum class MediaType { IMAGE, VIDEO }