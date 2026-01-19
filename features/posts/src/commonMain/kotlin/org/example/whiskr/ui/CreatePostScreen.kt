package org.example.whiskr.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.CreatePostInput
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.cancel
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
            Text(
                text = stringResource(Res.string.cancel),
                style = WhiskrTheme.typography.body.copy(fontWeight = FontWeight.Medium),
                color = WhiskrTheme.colors.onBackground,
                modifier = Modifier
                    .statusBarsPadding()
                    .customClickable(onClick = component::onBackClick)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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