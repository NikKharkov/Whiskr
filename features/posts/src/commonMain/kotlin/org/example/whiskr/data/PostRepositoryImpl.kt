package org.example.whiskr.data

import co.touchlab.kermit.Logger
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.io.readByteArray
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.dto.PagedResponse
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.UserInteraction

@Inject
class PostRepositoryImpl(
    private val postApiService: PostApiService
) : PostRepository {

    private val _newPost = MutableSharedFlow<Post>(extraBufferCapacity = 1)
    override val newPost: Flow<Post> = _newPost.asSharedFlow()

    private val _postUpdated = MutableSharedFlow<Post>(extraBufferCapacity = 1)
    override val postUpdated: Flow<Post> = _postUpdated.asSharedFlow()

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

    override suspend fun replyToPost(targetPostId: Long, text: String): Result<Post> {
        return runCatching {
            val request = CreateReplyRequest(targetPostId, text)
            val response = postApiService.replyToPost(request)

            _newPost.emit(response)
            response
        }
    }

    override suspend fun getReplies(postId: Long, page: Int): Result<PagedResponse<Post>> {
        return runCatching {
            postApiService.getReplies(postId = postId, page = page)
        }
    }

    override suspend fun notifyPostUpdated(post: Post) {
        _postUpdated.emit(post)
    }

    override suspend fun createPost(
        context: PlatformContext,
        text: String?,
        files: List<KmpFile>
    ): Result<Post> {
        return try {
            val preparedFiles = files.mapIndexed { index, file ->
                val bytes = file.readByteArray(context)
                val name = file.getName(context) ?: "file_$index"
                val extension = name.substringAfterLast('.', "").lowercase()

                val mimeType = when (extension) {
                    "mp4", "mov", "avi", "mkv" -> "video/mp4"
                    "png" -> "image/png"
                    else -> "image/jpeg"
                }

                Triple(bytes, name, mimeType)
            }

            val requestDto = CreatePostRequest(text)

            val multipartBody = MultiPartFormDataContent(
                formData {
                    append(
                        key = "request",
                        value = Json.encodeToString(requestDto),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        }
                    )

                    preparedFiles.forEach { (bytes, name, mimeType) ->
                        append(
                            key = "files",
                            value = bytes,
                            headers = Headers.build {
                                append(HttpHeaders.ContentDisposition, "filename=\"$name\"")
                                append(HttpHeaders.ContentType, mimeType)
                            }
                        )
                    }
                }
            )

            val response = postApiService.createPost(multipartBody)
            Logger.d { "Content sent successfully" }
            _newPost.emit(response)
            Result.success(response)
        } catch (e: Exception) {
            Logger.e(e) { "Error sending content" }
            Result.failure(e)
        }
    }
}