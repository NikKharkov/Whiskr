package org.example.whiskr.domain

import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.coroutines.flow.Flow
import org.example.whiskr.dto.PagedResponse
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.UserInteraction

interface PostRepository {

    val newPost: Flow<Post>
    val postUpdated: Flow<Post>

    suspend fun notifyPostUpdated(post: Post)
    suspend fun getFeed(page: Int): Result<PagedResponse<Post>>
    suspend fun toggleLike(postId: Long): Result<UserInteraction>
    suspend fun createRepost(originalPostId: Long, quote: String?): Result<Post>
    suspend fun getPostById(postId: Long): Result<Post>
    suspend fun createPost(
        context: PlatformContext,
        text: String?,
        files: List<KmpFile>
    ): Result<Post>
    suspend fun replyToPost(
        context: PlatformContext,
        targetPostId: Long,
        text: String,
        files: List<KmpFile>
    ): Result<Post>
    suspend fun getReplies(postId: Long, page: Int): Result<PagedResponse<Post>>
}