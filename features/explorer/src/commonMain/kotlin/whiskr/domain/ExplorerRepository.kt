package org.example.whiskr.domain

import org.example.whiskr.data.Post
import org.example.whiskr.dto.PagedResponse

interface ExplorerRepository {
    suspend fun getTrending(page: Int): Result<PagedResponse<Post>>
    suspend fun searchPosts(query: String, page: Int): Result<PagedResponse<Post>>
}