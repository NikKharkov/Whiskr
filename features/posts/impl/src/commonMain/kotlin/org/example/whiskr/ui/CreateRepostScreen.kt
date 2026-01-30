package org.example.whiskr.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.CreateRepostComponent
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.layouts.AdaptiveLayoutWithDialog
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.util.toCloudStorageUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.posts.impl.generated.resources.Res
import whiskr.features.posts.impl.generated.resources.add_comment_hint
import whiskr.features.posts.impl.generated.resources.ic_close
import whiskr.features.posts.impl.generated.resources.post_action
import whiskr.features.posts.impl.generated.resources.repost_title

@Composable
fun CreateRepostScreen(
    component: CreateRepostComponent
) {
    val model by component.model.subscribeAsState()
    val focusRequester = remember { FocusRequester() }

    AdaptiveLayoutWithDialog(
        isTablet = true,
        topBar = {
            SimpleTopBar(
                title = {
                    Text(
                        text = stringResource(Res.string.repost_title),
                        style = WhiskrTheme.typography.h3,
                        color = WhiskrTheme.colors.onBackground
                    )
                },
                icon = painterResource(Res.drawable.ic_close),
                onIconClick = component::onBackClick
            )
        }
    ) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        WhiskrTextField(
            value = model.text,
            onValueChange = component::onTextChanged,
            hint = stringResource(Res.string.add_comment_hint),
            enabled = !model.isReposting,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, WhiskrTheme.colors.outline, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AvatarPlaceholder(
                        avatarUrl = model.targetPost.author.avatarUrl?.toCloudStorageUrl(),
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = model.targetPost.author.displayName,
                        style = WhiskrTheme.typography.button,
                        color = WhiskrTheme.colors.onBackground
                    )
                }

                Text(
                    text = model.targetPost.content ?: "",
                    style = WhiskrTheme.typography.body,
                    color = WhiskrTheme.colors.onBackground,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        WhiskrButton(
            text = stringResource(Res.string.post_action),
            onClick = component::onRepostClick,
            isLoading = model.isReposting,
            enabled = !model.isReposting,
            modifier = Modifier.fillMaxWidth()
        )
    }
}