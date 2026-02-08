package ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import component.ChatDetailComponent
import data.ChatAttachmentDto
import data.ChatMessageDto
import data.UserState
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.layouts.CollapsingTopBarScaffold
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.MediaPreviewItem
import org.example.whiskr.util.toCloudStorageUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import util.toLastSeenText
import util.toMessageTime
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.ic_arrow_back
import whiskr.features.chat.generated.resources.ic_check_read
import whiskr.features.chat.generated.resources.ic_gallery
import whiskr.features.chat.generated.resources.ic_send
import whiskr.features.chat.generated.resources.online
import whiskr.features.chat.generated.resources.typing

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
                    component.onRemoveAttachment(
                        model.attachedFiles.indexOf(
                            file
                        )
                    )
                },
                onSendClick = { component.onSendMessage(platformContext) },
                isSending = model.connectionState == ChatDetailComponent.ConnectionState.CONNECTING
            )
        }
    }
}

@Composable
private fun ChatTopBar(
    modifier: Modifier = Modifier,
    model: ChatDetailComponent.Model,
    onBackClick: () -> Unit,
    onProfileClick: (String) -> Unit
) {
    val chatInfo = model.chatInfo
    val isTyping = model.typingUserIds.isNotEmpty()

    val isOnline = model.partnerStatus == UserState.ONLINE

    val statusText = when {
        isTyping -> stringResource(Res.string.typing)
        isOnline -> stringResource(Res.string.online) // Реальный онлайн!
        else -> chatInfo?.lastActivity?.toLastSeenText() ?: "" // Если оффлайн -> last seen
    }

    val statusColor = when {
        isTyping -> WhiskrTheme.colors.primary
        isOnline -> WhiskrTheme.colors.primary // Или зеленый, если хочешь
        else -> WhiskrTheme.colors.secondary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(WhiskrTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null,
                tint = WhiskrTheme.colors.onBackground
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .customClickable {
                    chatInfo?.partnerHandle?.let(onProfileClick)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarPlaceholder(
                avatarUrl = chatInfo?.partnerAvatar,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = chatInfo?.partnerName ?: "Chat",
                    style = WhiskrTheme.typography.h3.copy(fontSize = 16.sp),
                    color = WhiskrTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = statusText,
                    style = WhiskrTheme.typography.caption,
                    color = statusColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
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

@Composable
private fun MessageBubble(
    message: ChatMessageDto,
    isMe: Boolean,
    onMediaClick: (Int) -> Unit
) {
    val bubbleColor = if (isMe) WhiskrTheme.colors.primary else WhiskrTheme.colors.surface
    val textColor = if (isMe) Color.White else WhiskrTheme.colors.onBackground
    val timeColor = textColor.copy(alpha = 0.7f)
    val hasAttachments = message.attachments.isNotEmpty()

    // Логика цвета галочек:
    // Если прочитано -> Полный цвет (White или Accent)
    // Если не прочитано -> Полупрозрачный
    val readStatusTint = if (message.isRead) Color.White else Color.White.copy(alpha = 0.6f)

    val shape = RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 18.dp,
        bottomStart = if (isMe) 18.dp else 4.dp,
        bottomEnd = if (isMe) 4.dp else 18.dp
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .widthIn(min = 80.dp, max = 320.dp)
                .clip(shape)
                .background(bubbleColor)
        ) {
            // 1. Медиа
            if (hasAttachments) {
                ChatMediaCarousel(
                    attachments = message.attachments,
                    onMediaClick = onMediaClick,
                    modifier = Modifier.padding(2.dp)
                )
            }

            // 2. Текст + Время + Галочки
            if (!message.content.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                        // Если есть картинка, растягиваем текст, иначе обтягиваем
                        .then(if (hasAttachments) Modifier.fillMaxWidth() else Modifier)
                ) {
                    Text(
                        text = message.content,
                        style = WhiskrTheme.typography.body,
                        color = textColor,
                        // Отступ снизу, чтобы текст не наехал на время
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    // Группа: Время + Галочки
                    Row(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = message.createdAt.toMessageTime(),
                            style = WhiskrTheme.typography.caption.copy(fontSize = 10.sp),
                            color = timeColor
                        )

                        // Показываем галочки только для "Меня"
                        if (isMe) {
                            Icon(
                                // 🔥 ТВОЯ ИКОНКА ТУТ (ic_check_read или ic_done_all)
                                painter = painterResource(Res.drawable.ic_check_read),
                                contentDescription = null,
                                tint = readStatusTint,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            } else if (hasAttachments) {
                // Если текста нет — время и галочки на картинке
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp, bottom = 6.dp)
                ) {
                    // Фончик для читаемости на фото
                    Row(
                        modifier = Modifier
                            .offset(y = (-10).dp, x = (-4).dp)
                            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = message.createdAt.toMessageTime(),
                            style = WhiskrTheme.typography.caption.copy(fontSize = 10.sp),
                            color = Color.White
                        )

                        if (isMe) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_check_read),
                                contentDescription = null,
                                tint = if (message.isRead) Color.White else Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChanged: (String) -> Unit,
    files: List<KmpFile>,
    onFilesSelected: (List<KmpFile>) -> Unit,
    onRemoveFile: (KmpFile) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean
) {
    val launcher = rememberFilePickerLauncher(
        type = FilePickerFileType.ImageVideo,
        selectionMode = FilePickerSelectionMode.Multiple,
        onResult = onFilesSelected
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiskrTheme.colors.background)
            .navigationBarsPadding() // Отступ под системную навигацию
    ) {
        // Превью файлов (как в телеге над строкой)
        if (files.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(files) { file ->
                    MediaPreviewItem( // Твой существующий компонент
                        file = file,
                        onRemove = { onRemoveFile(file) },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
            HorizontalDivider(color = WhiskrTheme.colors.surface)
        }

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Кнопка медиа
            Icon(
                painter = painterResource(Res.drawable.ic_gallery),
                contentDescription = null,
                tint = WhiskrTheme.colors.primary,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .customClickable { launcher.launch() }
                    .padding(8.dp)
            )

            // Поле ввода
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(WhiskrTheme.colors.surface)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = "Message...",
                        style = WhiskrTheme.typography.body,
                        color = WhiskrTheme.colors.secondary
                    )
                }
                BasicTextField(
                    value = text,
                    onValueChange = onTextChanged,
                    textStyle = WhiskrTheme.typography.body.copy(color = WhiskrTheme.colors.onBackground),
                    cursorBrush = SolidColor(WhiskrTheme.colors.primary),
                    maxLines = 5
                )
            }

            // Кнопка отправки
            val canSend = text.isNotBlank() || files.isNotEmpty()
            val sendTint by animateColorAsState(
                if (canSend) WhiskrTheme.colors.primary else WhiskrTheme.colors.secondary
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .customClickable(enabled = canSend && !isSending, onClick = onSendClick),
                contentAlignment = Alignment.Center
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = WhiskrTheme.colors.primary
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.ic_send),
                        contentDescription = null,
                        tint = sendTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMediaCarousel(
    attachments: List<ChatAttachmentDto>,
    onMediaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (attachments.isEmpty()) return

    val pagerState = rememberPagerState { attachments.size }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.1f))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            val attachment = attachments[index]

            if (attachment.type == "VIDEO") {
                ChatVideoItem(
                    url = attachment.url.toCloudStorageUrl(),
                    onClick = { onMediaClick(index) }
                )
            } else {
                AsyncImage(
                    model = attachment.url.toCloudStorageUrl(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .customClickable { onMediaClick(index) }
                )
            }
        }

        if (attachments.size > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${attachments.size}",
                    color = Color.White,
                    style = WhiskrTheme.typography.caption.copy(fontSize = 10.sp)
                )
            }
        }
    }
}

@Composable
private fun ChatVideoItem(
    url: String,
    onClick: () -> Unit
) {
    val videoPlayerHost = remember(url) {
        MediaPlayerHost(
            mediaUrl = url,
            isMuted = true
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        VideoPlayerComposable(
            playerHost = videoPlayerHost,
            playerConfig = VideoPlayerConfig(
                showControls = false,
                isPauseResumeEnabled = false,
                isSeekBarVisible = false,
                isDurationVisible = false,
                isMuteControlEnabled = false,
                isFullScreenEnabled = false,
                isScreenLockEnabled = false,
                isZoomEnabled = false,
                isGestureVolumeControlEnabled = false,
                loadingIndicatorColor = WhiskrTheme.colors.primary,
                iconsTintColor = Color.White
            ),
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .customClickable(onClick = onClick)
        )
    }
}

