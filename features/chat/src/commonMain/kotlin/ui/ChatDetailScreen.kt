package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import component.detail.ChatDetailComponent
import data.ChatMessageDto
import org.example.whiskr.layouts.CollapsingTopBarScaffold
import ui.components.detail.ChatInputBar
import ui.components.detail.ChatTopBar
import ui.components.detail.MessageBubble
import util.LocalUser

@Composable
fun ChatDetailScreen(
    component: ChatDetailComponent
) {
    val model by component.model.subscribeAsState()
    val listState = rememberLazyListState()
    val platformContext = LocalPlatformContext.current
    val user = LocalUser.current?.profile

    CollapsingTopBarScaffold(
        topBarContentHeight = 64.dp,
        useStatusBarPadding = true,
        topBar = { modifier ->
            ChatTopBar(
                model = model,
                onBackClick = component::onBackClicked,
                onProfileClick = component::onProfileClick,
                modifier = modifier.padding(horizontal = 8.dp)
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            MessageList(
                messages = model.messages,
                currentUserId = user?.id ?: -1L,
                listState = listState,
                onMediaClick = { msg, index -> component.onAttachmentClicked(msg, index) },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    top = contentPadding.calculateTopPadding() + 8.dp,
                    bottom = 16.dp
                )
            )

            ChatInputBar(
                text = model.inputText,
                onTextChanged = component::onTypingInputChanged,
                files = model.attachedFiles,
                onFilesSelected = component::onAttachmentsSelected,
                onRemoveFile = { file ->
                    component.onRemoveAttachment(model.attachedFiles.indexOf(file))
                },
                onSendClick = { component.onSendMessage(platformContext) },
                isSending = model.connectionState == ChatDetailComponent.ConnectionState.CONNECTING
            )
        }
    }
}

@Composable
private fun MessageList(
    messages: List<ChatMessageDto>,
    currentUserId: Long,
    listState: LazyListState,
    onMediaClick: (ChatMessageDto, Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = messages,
            key = { it.id }
        ) { message ->
            MessageBubble(
                message = message,
                isMe = message.senderId == currentUserId,
                onMediaClick = { index -> onMediaClick(message, index) }
            )
        }
    }
}