package org.example.whiskr.data

import co.touchlab.kermit.Logger
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.getName
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.domain.MediaProcessingService
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.domain.UploadItem
import org.example.whiskr.dto.PagedResponse

@Inject
class PostRepositoryImpl(
    private val postApiService: PostApiService,
    private val mediaProcessingService: MediaProcessingService
) : PostRepository {

    override suspend fun getFeed(page: Int): Result<PagedResponse<Post>> {
        return runCatching { postApiService.getFeed(page) }
    }

    override suspend fun toggleLike(postId: Long): Result<UserInteraction> {
        return runCatching { postApiService.toggleLike(postId) }
    }

    override suspend fun createRepost(originalPostId: Long, quote: String?): Result<Post> {
        return runCatching {
            val request = CreateRepostRequest(originalPostId, quote)
            postApiService.createRepost(request)
        }
    }

    override suspend fun createPost(
        context: PlatformContext,
        text: String?,
        files: List<KmpFile>
    ): Result<Post> {
        return try {
            val isVideo = files.firstOrNull()?.getName(context)?.lowercase()
                ?.matches(Regex(".*\\.(mp4|mov|avi)$")) == true

            val uploadItems = mutableListOf<UploadItem>()
            var videoDuration: Int? = null

            if (isVideo) {
                val file = files.first()
                val processed = mediaProcessingService.processVideo(file)
                videoDuration = processed.durationSeconds

                uploadItems.add(
                    UploadItem(
                        filePath = processed.filePath,
                        filename = "video.mp4",
                        mimeType = processed.mimeType
                    )
                )
                uploadItems.add(
                    UploadItem(
                        bytes = processed.thumbnail.bytes,
                        filename = "thumbnail.jpg",
                        mimeType = processed.thumbnail.mimeType
                    )
                )
            } else {
                files.forEachIndexed { index, file ->
                    val processed = mediaProcessingService.processImage(file)
                    uploadItems.add(
                        UploadItem(
                            bytes = processed.bytes,
                            filename = "image_$index.jpg",
                            mimeType = processed.mimeType
                        )
                    )
                }
            }

            val requestDto = CreatePostRequest(text, videoDuration)

            val multipartBody = MultiPartFormDataContent(
                formData {
                    append(
                        key = "request",
                        value = Json.encodeToString(requestDto),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        }
                    )

                    uploadItems.forEach { item ->
                        val content = item.bytes ?: mediaProcessingService.readFile(item.filePath!!)

                        append(
                            key = "files",
                            value = content,
                            headers = Headers.build {
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "filename=\"${item.filename}\""
                                )
                                append(HttpHeaders.ContentType, item.mimeType)
                            }
                        )
                    }
                }
            )

            val response = postApiService.createPost(multipartBody)
            Logger.d { "Content sent successfully" }
            Result.success(response)
        } catch (e: Exception) {
            Logger.e(e) { "Error sending content" }
            Result.failure(e)
        }
    }
}