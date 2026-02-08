package component

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import data.ChatAttachmentDto
import data.ChatMessageDto
import data.ChatSocketService
import data.ChatStateEvent
import data.MessageType
import data.SendMessageRequest
import data.UserState
import domain.ChatRepository
import domain.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.PagingDelegate
import org.example.whiskr.component.componentScope
import org.example.whiskr.data.Media
import org.example.whiskr.data.MediaType
import kotlin.random.Random
import kotlin.time.Clock

@Inject
class DefaultChatDetailComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val initialChatId: Long,
    @Assisted private val targetUserId: Long,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onNavigateToProfile: (String) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<Media>, Int) -> Unit,
    private val chatRepository: ChatRepository,
    private val socketService: ChatSocketService,
    private val userRepository: UserRepository
) : ChatDetailComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private var typingJob: Job? = null
    private var isTypingSent = false
    private var pagingDelegate: PagingDelegate<ChatMessageDto>? = null
    private val resolvedChatId = MutableValue(initialChatId)

    private val _model = MutableValue(ChatDetailComponent.Model())
    override val model: Value<ChatDetailComponent.Model> = _model

    init {
        scope.launch {
            initializeComponent()
        }

        lifecycle.doOnDestroy {
            scope.launch { socketService.disconnect() }
        }
    }

    private suspend fun initializeComponent() {
        loadCurrentUser()

        val chatId = resolveChatAndSetupInitialState()
        if (chatId == null) {
            _model.update { it.copy(isError = true) }
            return
        }

        initializePaging(chatId)
        connectAndListenSocket(chatId)
    }

    private fun loadCurrentUser() {
        val userProfile = userRepository.user.value.profile
        if (userProfile != null) {
            _model.update { it.copy(currentUserId = userProfile.id) }
        }
    }

    private suspend fun resolveChatAndSetupInitialState(): Long? {
        if (initialChatId != -1L) return initialChatId

        if (targetUserId != -1L) {
            val chatDto = chatRepository.getOrCreatePrivateChat(targetUserId).getOrNull()
            if (chatDto != null) {
                val initialStatus = if (chatDto.isOnline) UserState.ONLINE else UserState.OFFLINE

                _model.update {
                    it.copy(
                        chatInfo = chatDto,
                        partnerStatus = initialStatus
                    )
                }
                resolvedChatId.value = chatDto.id
                return chatDto.id
            }
        }
        return null
    }

    private fun initializePaging(chatId: Long) {
        pagingDelegate = PagingDelegate(
            scope = scope,
            loader = { page -> chatRepository.getChatHistory(chatId, page) }
        ).also { delegate ->
            delegate.state.subscribe { pagingState ->
                _model.update { current ->
                    current.copy(
                        messages = pagingState.items,
                        isLoadingMore = pagingState.isLoadingMore,
                        isError = pagingState.isError
                    )
                }
            }
            delegate.firstLoad()
        }
    }

    private suspend fun connectAndListenSocket(chatId: Long) {
        _model.update { it.copy(connectionState = ChatDetailComponent.ConnectionState.CONNECTING) }

        try {
            socketService.connect()
            _model.update { it.copy(connectionState = ChatDetailComponent.ConnectionState.CONNECTED) }

            socketService.sendMarkAsRead(chatId)
            observeSocketEvents(chatId)

        } catch (e: Exception) {
            Logger.e(e) { "Socket connection failed" }
            _model.update { it.copy(connectionState = ChatDetailComponent.ConnectionState.DISCONNECTED) }
        }
    }

    private fun observeSocketEvents(chatId: Long) {
        scope.launch {
            socketService.observeMessages(chatId).collect { message ->
                handleIncomingMessage(chatId, message)
            }
        }

        scope.launch {
            socketService.observeChatStates(chatId).collect { event ->
                handleRemoteStateEvent(event)
            }
        }

        scope.launch {
            socketService.observePrivateEvents().collect { event ->
                if (event.chatId == chatId || event.chatId == -1L) {
                    handleRemoteStateEvent(event)
                }
            }
        }
    }

    private suspend fun handleIncomingMessage(chatId: Long, message: ChatMessageDto) {
        val currentUserId = _model.value.currentUserId

        if (message.senderId == currentUserId) {
            handleSelfMessage(message)
        } else {
            pagingDelegate?.prependItem(message)
            socketService.sendMarkAsRead(chatId)
        }
    }

    private fun handleSelfMessage(message: ChatMessageDto) {
        val isReplaced = pagingDelegate?.tryReplace(
            predicate = { temp ->
                temp.id < 0 &&
                        temp.content.orEmpty() == message.content.orEmpty() &&
                        temp.attachments.size == message.attachments.size
            },
            newItem = message
        ) ?: false

        if (!isReplaced) {
            pagingDelegate?.prependItem(message)
        }
    }

    private fun handleRemoteStateEvent(event: ChatStateEvent) {
        if (event.userId == _model.value.currentUserId) return

        _model.update { state ->
            val typingUsers = state.typingUserIds.toMutableSet()
            var messages = state.messages
            var partnerStatus = state.partnerStatus

            when (event.state) {
                UserState.TYPING -> typingUsers.add(event.userId)

                UserState.STOP_TYPING -> typingUsers.remove(event.userId)

                UserState.ONLINE -> {
                    partnerStatus = UserState.ONLINE
                }

                UserState.OFFLINE -> {
                    partnerStatus = UserState.OFFLINE
                    typingUsers.remove(event.userId)
                }

                UserState.READ -> {
                    partnerStatus = UserState.ONLINE
                    typingUsers.remove(event.userId)

                    messages = messages.map { msg ->
                        if (msg.senderId == state.currentUserId && !msg.isRead) {
                            msg.copy(isRead = true)
                        } else msg
                    }
                }
            }

            state.copy(
                typingUserIds = typingUsers,
                messages = messages,
                partnerStatus = partnerStatus
            )
        }
    }

    override fun onSendMessage(platformContext: PlatformContext) {
        val state = _model.value
        val chatId = state.chatInfo?.id ?: resolvedChatId.value
        if (chatId == -1L) return

        val text = state.inputText
        val files = state.attachedFiles.toList()
        val replyTo = state.replyToMessage

        if (text.isBlank() && files.isEmpty()) return

        clearInputState()

        scope.launch {
            sendMessageInternal(platformContext, chatId, text, files, replyTo, state.currentUserId)
        }
    }

    private fun clearInputState() {
        _model.update {
            it.copy(
                inputText = "",
                attachedFiles = emptyList(),
                replyToMessage = null
            )
        }
    }

    private suspend fun sendMessageInternal(
        context: PlatformContext,
        chatId: Long,
        text: String,
        files: List<KmpFile>,
        replyTo: ChatMessageDto?,
        senderId: Long
    ) {
        try {
            val urls = if (files.isNotEmpty()) {
                chatRepository.uploadMedia(context, chatId, files).getOrElse { throw it }
            } else emptyList()

            val content = text.trim().ifBlank { null }
            if (content == null && urls.isEmpty()) return

            val attachments = urls.map { url ->
                val isVideo = url.isVideoExtension()
                ChatAttachmentDto(url, if (isVideo) "VIDEO" else "IMAGE")
            }

            val type = if (attachments.any { it.type == "VIDEO" }) MessageType.VIDEO else MessageType.IMAGE

            pagingDelegate?.prependItem(
                ChatMessageDto(
                    id = -Random.nextLong(1, Long.MAX_VALUE),
                    chatId = chatId,
                    senderId = senderId,
                    senderName = "",
                    senderAvatar = null,
                    content = content,
                    type = type,
                    attachments = attachments,
                    replyToId = replyTo?.id,
                    createdAt = Clock.System.now(),
                    isRead = false
                )
            )

            socketService.sendMessage(chatId, SendMessageRequest(content, urls, type, replyTo?.id))

        } catch (e: Exception) {
            Logger.e(e) { "Failed to send message" }
        }
    }

    override fun onTypingInputChanged(text: String) {
        val chatId = _model.value.chatInfo?.id ?: resolvedChatId.value
        if (chatId == -1L) return

        _model.update { it.copy(inputText = text) }

        if (!isTypingSent) {
            scope.launch { socketService.sendTyping(chatId, true) }
            isTypingSent = true
        }

        typingJob?.cancel()
        typingJob = scope.launch {
            delay(2000)
            socketService.sendTyping(chatId, false)
            isTypingSent = false
        }
    }

    override fun onAttachmentClicked(message: ChatMessageDto, startIndex: Int) {
        if (message.attachments.isEmpty()) return

        val mediaList = message.attachments.mapIndexed { index, attachment ->
            Media(
                id = message.id + index,
                url = attachment.url,
                type = if (attachment.type == "VIDEO") MediaType.VIDEO else MediaType.IMAGE,
                width = 0,
                height = 0,
                thumbnailUrl = null,
                duration = null
            )
        }
        onNavigateToMediaViewer(mediaList, startIndex)
    }

    override fun onAttachmentsSelected(files: List<KmpFile>) {
        _model.update { state ->
            val currentSize = state.attachedFiles.size
            val available = 10 - currentSize
            if (available > 0) {
                state.copy(attachedFiles = state.attachedFiles + files.take(available))
            } else state
        }
    }

    override fun onRemoveAttachment(index: Int) {
        _model.update { state ->
            val list = state.attachedFiles.toMutableList()
            if (index in list.indices) {
                list.removeAt(index)
                state.copy(attachedFiles = list)
            } else state
        }
    }

    override fun onLoadMore() {
        pagingDelegate?.loadMore()
    }

    override fun onReplyClick(message: ChatMessageDto) {
        _model.update { it.copy(replyToMessage = message) }
    }

    override fun onReplyCancel() {
        _model.update { it.copy(replyToMessage = null) }
    }

    override fun onProfileClick(handle: String) = onNavigateToProfile(handle)
    override fun onBackClicked() = onBack()
    override fun onMessageClick(message: ChatMessageDto) {
        TODO()
    }

    private fun String.isVideoExtension(): Boolean {
        return endsWith(".mp4", true) ||
                endsWith(".mov", true) ||
                endsWith(".mkv", true)
    }
}