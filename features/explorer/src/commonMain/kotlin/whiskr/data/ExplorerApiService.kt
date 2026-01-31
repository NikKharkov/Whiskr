package org.example.whiskr.data

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import org.example.whiskr.dto.PagedResponse

interface ExplorerApiService {

    @GET("post/trending")
    suspend fun getTrending(
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<Post>

    @GET("post/search")
    suspend fun searchPosts(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<Post>
}