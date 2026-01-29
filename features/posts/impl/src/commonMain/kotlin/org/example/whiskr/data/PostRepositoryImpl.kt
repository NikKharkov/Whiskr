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

    override suspend fun getPostById(postId: Long): Result<Post> {
        return runCatching { postApiService.getPostById(postId) }
    }

    override suspend fun getPostsByHashtag(tag: String, page: Int): Result<PagedResponse<Post>> {
        return runCatching {
            postApiService.getPostsByHashtag(tag, page)
        }
    }

    override suspend fun getReplies(postId: Long, page: Int): Result<PagedResponse<Post>> {
        return runCatching {
            postApiService.getReplies(postId = postId, page = page)
        }
    }

    override suspend fun getPostsByHandle(handle: String, page: Int): Result<PagedResponse<Post>> {
        return runCatching {
            postApiService.getPostsByHandle(handle, page)
        }
    }

    override suspend fun notifyPostUpdated(post: Post) {
        _postUpdated.emit(post)
    }

    override suspend fun createPost(
        context: PlatformContext,
        text: String?,
        files: List<KmpFile>,
        attachedUrls: List<String>
    ): Result<Post> {
        return try {
            val requestDto = CreatePostRequest(
                text = text,
                attachedMediaUrls = attachedUrls.ifEmpty { null }
            )

            val multipartBody = buildMultipartRequest(
                context = context,
                jsonPartName = "request",
                jsonPartContent = Json.encodeToString(requestDto),
                files = files
            )

            val response = postApiService.createPost(multipartBody)
            Logger.d { "Post created successfully" }
            _newPost.emit(response)
            Result.success(response)
        } catch (e: Exception) {
            Logger.e(e) { "Error creating post" }
            Result.failure(e)
        }
    }

    override suspend fun replyToPost(
        context: PlatformContext,
        targetPostId: Long,
        text: String,
        files: List<KmpFile>
    ): Result<Post> {
        return try {
            val requestDto = CreateReplyRequest(targetPostId, text)

            val multipartBody = buildMultipartRequest(
                context = context,
                jsonPartName = "request",
                jsonPartContent = Json.encodeToString(requestDto),
                files = files
            )

            val response = postApiService.replyToPost(multipartBody)

            _newPost.emit(response)
            Result.success(response)
        } catch (e: Exception) {
            Logger.e(e) { "Error creating reply" }
            Result.failure(e)
        }
    }

    private suspend fun buildMultipartRequest(
        context: PlatformContext,
        jsonPartName: String,
        jsonPartContent: String,
        files: List<KmpFile>
    ): MultiPartFormDataContent {
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

        return MultiPartFormDataContent(
            formData {
                append(
                    key = jsonPartName,
                    value = jsonPartContent,
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
    }
}