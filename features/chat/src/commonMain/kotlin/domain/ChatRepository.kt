package domain

import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import data.ChatDto
import data.ChatMessageDto
import org.example.whiskr.dto.PagedResponse

interface ChatRepository {
    suspend fun getMyChats(page: Int): Result<PagedResponse<ChatDto>>
    suspend fun getChatHistory(chatId: Long, page: Int): Result<PagedResponse<ChatMessageDto>>
    suspend fun uploadMedia(context: PlatformContext, chatId: Long, files: List<KmpFile>): Result<List<String>>
    suspend fun getOrCreatePrivateChat(targetUserId: Long): Result<ChatDto>
}