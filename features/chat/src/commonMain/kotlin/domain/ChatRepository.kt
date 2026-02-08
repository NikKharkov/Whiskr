package domain

import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import data.ChatDto
import data.ChatMessageDto
import org.example.whiskr.dto.PagedResponse
import org.example.whiskr.dto.ProfileResponse

interface ChatRepository {
    suspend fun getMyChats(page: Int): Result<PagedResponse<ChatDto>>
    suspend fun getChat(chatId: Long): Result<ChatDto>
    suspend fun getChatHistory(chatId: Long, page: Int): Result<PagedResponse<ChatMessageDto>>
    suspend fun uploadMedia(context: PlatformContext, chatId: Long, files: List<KmpFile>): Result<List<String>>
    suspend fun getOrCreatePrivateChat(targetUserId: Long): Result<ChatDto>
    suspend fun searchUsers(query: String): Result<List<ProfileResponse>>
}