package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.reply.CreateReplyComponent
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.util.rememberRelativeTime
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.cancel
import whiskr.features.posts.generated.resources.reply
import whiskr.features.posts.generated.resources.reply_placeholder
import whiskr.features.posts.generated.resources.replying_to

@Composable
fun CreateReplyScreen(
    component: CreateReplyComponent
) {
    val model by component.model.subscribeAsState()
    val user = LocalUser.current

    Scaffold(
        topBar = {
            ReplyTopBar(
                isSending = model.isSending,
                isSendEnabled = model.text.isNotBlank(),
                onCancel = component::onBackClick,
                onSend = component::onSendClick,
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = WhiskrTheme.colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                            .padding(vertical = 4.dp)
                            .background(WhiskrTheme.colors.outline.copy(alpha = 0.5f))
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                            color = WhiskrTheme.colors.onBackground,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Text(
                        text = "${stringResource(Res.string.replying_to)} ${model.targetPost.author.handle}",
                        style = WhiskrTheme.typography.caption,
                        color = WhiskrTheme.colors.secondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AvatarPlaceholder(avatarUrl = user?.profile?.avatarUrl)

                Box(modifier = Modifier.weight(1f)) {
                    if (model.text.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.reply_placeholder),
                            style = WhiskrTheme.typography.h3.copy(fontWeight = FontWeight.Normal),
                            color = WhiskrTheme.colors.secondary.copy(alpha = 0.5f)
                        )
                    }

                    BasicTextField(
                        value = model.text,
                        onValueChange = component::onTextChanged,
                        textStyle = WhiskrTheme.typography.h3.copy(
                            fontWeight = FontWeight.Normal,
                            color = WhiskrTheme.colors.onBackground
                        ),
                        cursorBrush = SolidColor(WhiskrTheme.colors.primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ReplyTopBar(
    isSending: Boolean,
    isSendEnabled: Boolean,
    onCancel: () -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(Res.string.cancel),
            style = WhiskrTheme.typography.body.copy(fontWeight = FontWeight.Medium),
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.customClickable(onClick = onCancel)
        )

        WhiskrButton(
            text = stringResource(Res.string.reply),
            onClick = onSend,
            enabled = isSendEnabled,
            isLoading = isSending,
            shape = CircleShape,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp),
            containerColor = WhiskrTheme.colors.primary,
            contentColor = Color.White
        )
    }
}
