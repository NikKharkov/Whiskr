package org.example.whiskr.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.ui.CreatePostInput

@Composable
fun CreatePostLayout(
    component: CreatePostComponent,
    userAvatar: String?,
    modifier: Modifier = Modifier
) {
    val state by component.model.subscribeAsState()
    val context = LocalPlatformContext.current

    val launcher = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Multiple
    ) { files -> component.onImagesSelected(files) }

    CreatePostInput(
        text = state.text,
        userAvatar = userAvatar,
        selectedFiles = state.files,
        isSending = state.isSending,
        onTextChange = component::onTextChanged,
        onRemoveFile = component::onRemoveFile,
        onPickImagesClick = {
            if (state.files.size < 4) launcher.launch()
        },
        onSendClick = { component.onSendClick(context) },
        modifier = modifier
    )
}
