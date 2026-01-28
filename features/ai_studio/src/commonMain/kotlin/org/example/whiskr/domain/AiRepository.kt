package org.example.whiskr.domain

import org.example.whiskr.data.AiArtStyle
import org.example.whiskr.data.AiGalleryItemDto
import org.example.whiskr.data.AiGenerationResponseDto
import org.example.whiskr.dto.PagedResponse

interface AiRepository {
    suspend fun generateImage(
        prompt: String,
        style: AiArtStyle?,
        imageBytes: ByteArray?
    ): AiGenerationResponseDto

    suspend fun getGallery(page: Int): PagedResponse<AiGalleryItemDto>
}