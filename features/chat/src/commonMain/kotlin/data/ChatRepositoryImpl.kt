package data

import co.touchlab.kermit.Logger
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.io.readByteArray
import domain.ChatRepository
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.dto.PagedResponse

@Inject
class ChatRepositoryImpl(
    private val chatApiService: ChatApiService
) : ChatRepository {

    override suspend fun getMyChats(page: Int): Result<PagedResponse<ChatDto>> {
        return runCatching { chatApiService.getMyChats(page) }
    }

    override suspend fun getChatHistory(chatId: Long, page: Int): Result<PagedResponse<ChatMessageDto>> {
        return runCatching { chatApiService.getChatHistory(chatId, page) }
    }

    override suspend fun getOrCreatePrivateChat(targetUserId: Long): Result<ChatDto> {
        return runCatching {
            chatApiService.createPrivateChat(targetUserId)
        }
    }

    override suspend fun uploadMedia(
        context: PlatformContext,
        chatId: Long,
        files: List<KmpFile>
    ): Result<List<String>> {
        return try {
            if (files.isEmpty()) return Result.success(emptyList())

            val multipartBody = buildMultipartRequest(context, files)

            val responseMap = chatApiService.uploadMedia(chatId, multipartBody)

            val urls = responseMap["urls"]
                ?: throw IllegalStateException("Backend didn't return urls")

            Result.success(urls)
        } catch (e: Exception) {
            Logger.e(e) { "Error uploading chat media" }
            Result.failure(e)
        }
    }

    private suspend fun buildMultipartRequest(
        context: PlatformContext,
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