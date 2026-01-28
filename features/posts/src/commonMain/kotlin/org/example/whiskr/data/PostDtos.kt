package org.example.whiskr.data

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val text: String?,
    val attachedMediaUrls: List<String>? = null
)

@Serializable
data class CreateRepostRequest(
    val originalPostId: Long,
    val quoteText: String? = null
)

@Serializable
data class CreateReplyRequest(
    val targetPostId: Long,
    val text: String
)