package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.add_image
import whiskr.features.posts.generated.resources.ic_gallery
import whiskr.features.posts.generated.resources.post
import whiskr.features.posts.generated.resources.whats_happening

@Composable
fun CreatePostLayout(
    component: CreatePostComponent,
    userAvatar: String?,
    modifier: Modifier = Modifier
) {
    val state by component.model.subscribeAsState()
    val context = LocalPlatformContext.current

    val launcher = rememberFilePickerLauncher(
        type = FilePickerFileType.ImageVideo,
        selectionMode = FilePickerSelectionMode.Multiple
    ) { files -> component.onMediaSelected(files) }

    CreatePostInput(
        text = state.text,
        userAvatar = userAvatar,
        selectedFiles = state.files,
        isSending = state.isSending,
        onTextChange = component::onTextChanged,
        onRemoveFile = component::onRemoveFile,
        onPickImagesClick = {
            if (state.files.size < 10) launcher.launch()
        },
        onSendClick = { component.onSendClick(context) },
        modifier = modifier
    )
}

@Composable
private fun CreatePostInput(
    text: String,
    userAvatar: String?,
    onTextChange: (String) -> Unit,
    selectedFiles: List<KmpFile>,
    onRemoveFile: (KmpFile) -> Unit,
    onPickImagesClick: () -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        AvatarPlaceholder(
            avatarUrl = userAvatar,
            modifier = Modifier.padding(top = 4.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                textStyle = WhiskrTheme.typography.body,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.whats_happening),
                        color = WhiskrTheme.colors.secondary,
                        style = WhiskrTheme.typography.h3.copy(fontWeight = FontWeight.Normal)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = WhiskrTheme.colors.onBackground,
                )
            )

            if (selectedFiles.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedFiles) { file ->
                        MediaPreviewItem(
                            file = file,
                            onRemove = { onRemoveFile(file) },
                            modifier = Modifier
                                .width(160.dp)
                                .height(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_gallery),
                    contentDescription = stringResource(Res.string.add_image),
                    tint = WhiskrTheme.colors.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .customClickable(onClick = onPickImagesClick)
                )

                WhiskrButton(
                    onClick = onSendClick,
                    enabled = !isSending && (text.isNotBlank() || selectedFiles.isNotEmpty()),
                    text = stringResource(Res.string.post),
                    isLoading = isSending,
                    contentColor = Color.White,
                    modifier = Modifier.widthIn(min = 100.dp)
                )
            }
        }
    }
}