package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import org.example.whiskr.component.CreateReplyComponent
import org.example.whiskr.component.reply.FakeCreateReplyComponent
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.ComposerTopBar
import org.example.whiskr.util.mockPostForReply
import org.example.whiskr.util.mockUserState
import org.example.whiskr.util.rememberRelativeTime
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import whiskr.features.posts.impl.generated.resources.Res
import whiskr.features.posts.impl.generated.resources.reply
import whiskr.features.posts.impl.generated.resources.reply_placeholder
import whiskr.features.posts.impl.generated.resources.replying_to

@Composable
fun CreateReplyScreen(
    component: CreateReplyComponent
) {
    val user = LocalUser.current
    val context = LocalPlatformContext.current
    val model by component.model.subscribeAsState()

    Scaffold(
        topBar = {
            ComposerTopBar(
                onCancel = component::onBackClick,
                onSend = { component.onSendClick(context) },
                isSending = model.isSending,
                isSendEnabled = model.text.isNotBlank() || model.files.isNotEmpty(),
                actionLabel = stringResource(Res.string.reply),
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = WhiskrTheme.colors.background,
        contentWindowInsets = WindowInsets.ime
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(40.dp)
                ) {
                    AvatarPlaceholder(
                        avatarUrl = model.targetPost.author.avatarUrl,
                        modifier = Modifier.size(40.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .width(2.dp)
                            .padding(top = 4.dp)
                            .background(WhiskrTheme.colors.outline.copy(alpha = 0.5f))
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = model.targetPost.author.displayName,
                            style = WhiskrTheme.typography.h3,
                            color = WhiskrTheme.colors.onBackground
                        )

                        Text(
                            text = "${model.targetPost.author.handle} · ${rememberRelativeTime(model.targetPost.createdAt)}",
                            style = WhiskrTheme.typography.body,
                            color = WhiskrTheme.colors.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (!model.targetPost.content.isNullOrBlank()) {
                        Text(
                            text = model.targetPost.content!!,
                            style = WhiskrTheme.typography.body,
                            color = WhiskrTheme.colors.onBackground
                        )
                    }

                    Text(
                        text = "${stringResource(Res.string.replying_to)} ${model.targetPost.author.handle}",
                        style = WhiskrTheme.typography.caption,
                        color = WhiskrTheme.colors.secondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            CreatePostInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                avatarUrl = user?.profile?.avatarUrl,
                text = model.text,
                files = model.files,
                isSending = model.isSending,
                placeholderText = stringResource(Res.string.reply_placeholder),
                onTextChanged = component::onTextChanged,
                onFilesSelected = component::onMediaSelected,
                onRemoveFile = component::onRemoveFile,
                onSendClick = { component.onSendClick(context) }
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun CreateReplyScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        CompositionLocalProvider(LocalUser provides mockUserState) {
            CreateReplyScreen(
                component = FakeCreateReplyComponent()
            )
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
private fun CreateReplyScreenDarkThemePreview() {
    WhiskrTheme(isDarkTheme = true) {
        CompositionLocalProvider(LocalUser provides mockUserState) {
            CreateReplyScreen(
                component = FakeCreateReplyComponent(
                    initialModel = CreateReplyComponent.Model(
                        targetPost = mockPostForReply,
                        text = "Dark mode reply...",
                        isSending = true
                    )
                )
            )
        }
    }
}

@Preview(name = "Tablet", widthDp = 891, showBackground = true)
@Composable
private fun CreateReplyScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = false) {
        CompositionLocalProvider(LocalUser provides mockUserState) {
            CreateReplyScreen(
                component = FakeCreateReplyComponent()
            )
        }
    }
}