package org.example.whiskr.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.request.forms.MultiPartFormDataContent
import org.example.whiskr.dto.PagedResponse

interface PostApiService {

    @GET("post/feed")
    suspend fun getFeed(
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<Post>

    @Headers("Content-Type: application/json")
    @POST("post/{postId}/like")
    suspend fun toggleLike(@Path("postId") postId: Long): UserInteraction

    @Headers("Content-Type: application/json")
    @POST("post/repost")
    suspend fun createRepost(@Body request: CreateRepostRequest): Post

    @POST("post/create")
    suspend fun createPost(@Body body: MultiPartFormDataContent): Post

    @POST("post/reply")
    suspend fun replyToPost(@Body body: MultiPartFormDataContent): Post

    @GET("post/{postId}")
    suspend fun getPostById(@Path("postId") postId: Long): Post

    @GET("post/hashtag/{tag}")
    suspend fun getPostsByHashtag(
        @Path("tag") tag: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<Post>

    @GET("post/{postId}/replies")
    suspend fun getReplies(
        @Path("postId") postId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<Post>

    @GET("post/user/{handle}")
    suspend fun getPostsByHandle(
        @Path("handle") handle: String,
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<Post>
}