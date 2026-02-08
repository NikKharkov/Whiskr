package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import data.ChatDto
import data.ChatMessageDto
import data.UserState
import org.example.whiskr.data.Media

interface ChatDetailComponent {

    val model: Value<Model>

    fun onBackClicked()
    fun onSendMessage(platformContext: PlatformContext)
    fun onTypingInputChanged(text: String)
    fun onLoadMore()
    fun onAttachmentsSelected(files: List<KmpFile>)
    fun onRemoveAttachment(index: Int)
    fun onMessageClick(message: ChatMessageDto)
    fun onReplyClick(message: ChatMessageDto)
    fun onReplyCancel()
    fun onAttachmentClicked(message: ChatMessageDto, startIndex: Int)
    fun onProfileClick(handle: String)

    data class Model(
        val chatInfo: ChatDto? = null,
        val messages: List<ChatMessageDto> = emptyList(),
        val inputText: String = "",
        val attachedFiles: List<KmpFile> = emptyList(),
        val replyToMessage: ChatMessageDto? = null,
        val typingUserIds: Set<Long> = emptySet(),
        val currentUserId: Long = -1L,
        val partnerStatus: UserState = UserState.OFFLINE,
        val connectionState: ConnectionState = ConnectionState.CONNECTING,
        val isLoadingMore: Boolean = true,
        val isError: Boolean = false
    )

    enum class ConnectionState {
        CONNECTING, CONNECTED, DISCONNECTED
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            chatId: Long,
            userId: Long,
            onBack: () -> Unit,
            onNavigateToProfile: (String) -> Unit,
            onNavigateToMediaViewer: (List<Media>, Int) -> Unit
        ): ChatDetailComponent
    }
}