package data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.request.forms.MultiPartFormDataContent
import org.example.whiskr.dto.PagedResponse

interface ChatApiService {

    @GET("chats")
    suspend fun getMyChats(
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<ChatDto>

    @GET("chats/{chatId}/messages")
    suspend fun getChatHistory(
        @Path("chatId") chatId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int = 50
    ): PagedResponse<ChatMessageDto>

    @POST("chats/{chatId}/media")
    suspend fun uploadMedia(
        @Path("chatId") chatId: Long,
        @Body body: MultiPartFormDataContent
    ): Map<String, List<String>>

    @POST("chats/private")
    suspend fun createPrivateChat(
        @Query("targetUserId") targetUserId: Long
    ): ChatDto
}