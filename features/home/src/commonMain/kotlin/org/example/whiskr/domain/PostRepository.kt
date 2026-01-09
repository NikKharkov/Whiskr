package org.example.whiskr.domain

import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import org.example.whiskr.data.Post
import org.example.whiskr.data.UserInteraction
import org.example.whiskr.dto.PagedResponse

interface PostRepository {
    suspend fun getFeed(page: Int): Result<PagedResponse<Post>>

    suspend fun toggleLike(postId: Long): Result<UserInteraction>

    suspend fun createRepost(originalPostId: Long, quote: String?): Result<Post>

    suspend fun createPost(
        context: PlatformContext,
        text: String?,
        files: List<KmpFile>
    ): Result<Post>
}