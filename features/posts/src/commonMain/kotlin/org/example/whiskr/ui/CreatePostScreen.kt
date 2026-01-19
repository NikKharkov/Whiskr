package org.example.whiskr.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.ComposerTopBar
import org.example.whiskr.ui.components.CreatePostInput
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.whats_happening

@Composable
fun CreatePostScreen(component: CreatePostComponent) {
    val user = LocalUser.current
    val context = LocalPlatformContext.current
    val model by component.model.subscribeAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiskrTheme.colors.background,
        topBar = {
            ComposerTopBar(
                onCancel = component::onBackClick,
                onSend = { component.onSendClick(context) },
                isSending = model.isSending,
                isSendEnabled = model.text.isNotBlank() || model.files.isNotEmpty(),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { innerPadding ->
        CreatePostInput(
            modifier = Modifier.padding(innerPadding),
            myAvatarUrl = user?.profile?.avatarUrl,
            text = model.text,
            files = model.files,
            isSending = model.isSending,
            placeholderText = stringResource(Res.string.whats_happening),
            onTextChanged = component::onTextChanged,
            onFilesSelected = component::onMediaSelected,
            onRemoveFile = component::onRemoveFile,
            onSendClick = { component.onSendClick(context) }
        )
    }
}