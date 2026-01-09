package org.example.whiskr.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T>(
    val content: List<T>,
    val last: Boolean,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int
)