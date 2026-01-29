package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import org.example.whiskr.component.CreatePostComponent
import org.example.whiskr.ui.CreatePostInput
import org.jetbrains.compose.resources.stringResource
import whiskr.features.home.generated.resources.Res
import whiskr.features.home.generated.resources.whats_happening

@Composable
fun CreatePostLayout(
    component: CreatePostComponent,
    userAvatar: String?,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()
    val context = LocalPlatformContext.current

    CreatePostInput(
        modifier = modifier.fillMaxWidth(),
        avatarUrl = userAvatar,
        text = model.text,
        files = model.files,
        isSending = model.isSending,
        placeholderText = stringResource(Res.string.whats_happening),
        onTextChanged = component::onTextChanged,
        onFilesSelected = component::onMediaSelected,
        onRemoveFile = component::onRemoveFile,
        onSendClick = { component.onSendClick(context) },
        showSendButton = true
    )
}