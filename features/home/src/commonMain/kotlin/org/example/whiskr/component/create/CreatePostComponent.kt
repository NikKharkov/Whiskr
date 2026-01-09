package org.example.whiskr.component.create

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import org.example.whiskr.data.Post

interface CreatePostComponent {
    val model: Value<Model>

    fun onTextChanged(text: String)
    fun onImagesSelected(files: List<KmpFile>)
    fun onRemoveFile(file: KmpFile)
    fun onSendClick(context: PlatformContext)
    fun onBackClick()

    data class Model(
        val text: String = "",
        val files: List<KmpFile> = emptyList(),
        val isSending: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onPostCreated: (Post) -> Unit,
            onBack: () -> Unit
        ): CreatePostComponent
    }
}