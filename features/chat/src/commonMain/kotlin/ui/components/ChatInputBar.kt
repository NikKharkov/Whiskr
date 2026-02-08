package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.example.whiskr.components.MediaPreviewItem
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.ic_gallery
import whiskr.features.chat.generated.resources.ic_send
import whiskr.features.chat.generated.resources.inputbar_placeholder

@Composable
fun ChatInputBar(
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
            .navigationBarsPadding()
    ) {
        if (files.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(files) { file ->
                    MediaPreviewItem(
                        file = file,
                        onRemove = { onRemoveFile(file) },
                        modifier = Modifier
                            .size(80.dp)
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

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(WhiskrTheme.colors.surface)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.inputbar_placeholder),
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

            val canSend = text.isNotBlank() || files.isNotEmpty()

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
                        tint = if (canSend) WhiskrTheme.colors.primary else WhiskrTheme.colors.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}