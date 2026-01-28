package org.example.whiskr.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.request.forms.MultiPartFormDataContent
import org.example.whiskr.dto.PagedResponse

interface AiApiService {
    @POST("ai/generate")
    suspend fun generateImage(@Body body: MultiPartFormDataContent): AiGenerationResponseDto

    @GET("ai/gallery")
    suspend fun getGallery(
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<AiGalleryItemDto>
}