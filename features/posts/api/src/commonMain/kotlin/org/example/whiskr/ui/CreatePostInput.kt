package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.components.MediaPreviewItem
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.RemoteMediaPreviewItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.posts.api.generated.resources.Res
import whiskr.features.posts.api.generated.resources.ic_gallery
import whiskr.features.posts.api.generated.resources.post

@Composable
fun CreatePostInput(
    avatarUrl: String?,
    text: String,
    files: List<KmpFile>,
    attachedUrls: List<String> = emptyList(),
    onRemoveUrl: ((String) -> Unit) = {},
    isSending: Boolean,
    placeholderText: String,
    onTextChanged: (String) -> Unit,
    onFilesSelected: (List<KmpFile>) -> Unit,
    onRemoveFile: (KmpFile) -> Unit,
    onSendClick: () -> Unit,
    showSendButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    val totalMediaCount = files.size + attachedUrls.size

    val launcher = rememberFilePickerLauncher(
        type = FilePickerFileType.ImageVideo,
        selectionMode = FilePickerSelectionMode.Multiple,
        onResult = onFilesSelected
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        AvatarPlaceholder(avatarUrl = avatarUrl)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (text.isEmpty()) {
                    Text(
                        text = placeholderText,
                        color = WhiskrTheme.colors.secondary,
                        style = WhiskrTheme.typography.h3.copy(fontWeight = FontWeight.Normal),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                BasicTextField(
                    value = text,
                    onValueChange = onTextChanged,
                    textStyle = WhiskrTheme.typography.body.copy(
                        color = WhiskrTheme.colors.onBackground
                    ),
                    cursorBrush = SolidColor(WhiskrTheme.colors.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            if (totalMediaCount > 0) {
                LazyRow(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(attachedUrls) { url ->
                        RemoteMediaPreviewItem(
                            url = url,
                            onRemove = { onRemoveUrl(url) },
                            modifier = Modifier
                                .width(160.dp)
                                .height(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                    items(files) { file ->
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
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_gallery),
                    contentDescription = "Add media",
                    tint = if (files.size >= 10) WhiskrTheme.colors.secondary else WhiskrTheme.colors.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .customClickable {
                            if (files.size < 10) launcher.launch()
                        }
                )

                if (showSendButton) {
                    WhiskrButton(
                        onClick = onSendClick,
                        enabled = !isSending && (text.isNotBlank() || files.isNotEmpty()),
                        text = stringResource(Res.string.post),
                        isLoading = isSending,
                        contentColor = Color.White,
                        modifier = Modifier.widthIn(min = 100.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}