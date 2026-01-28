package org.example.whiskr.data

import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.domain.AiRepository
import org.example.whiskr.dto.PagedResponse

@Inject
class AiRepositoryImpl(
    private val aiApiService: AiApiService
) : AiRepository {

    override suspend fun generateImage(
        prompt: String,
        style: AiArtStyle?,
        imageBytes: ByteArray?
    ): AiGenerationResponseDto {

        val requestDto = AiGenerationRequestDto(prompt, style)

        val multipartBody = MultiPartFormDataContent(
            formData {
                append(
                    key = "request",
                    value = Json.encodeToString(requestDto),
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    }
                )

                if (imageBytes != null) {
                    append(
                        key = "file",
                        value = imageBytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"source_image.jpg\"")
                        }
                    )
                }
            }
        )

        return aiApiService.generateImage(multipartBody)
    }

    override suspend fun getGallery(page: Int): PagedResponse<AiGalleryItemDto> {
        return aiApiService.getGallery(page)
    }
}