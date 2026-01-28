package org.example.whiskr.component.create

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import org.example.whiskr.dto.Post

interface CreatePostComponent {
    val model: Value<Model>

    fun onTextChanged(text: String)
    fun onMediaSelected(files: List<KmpFile>)
    fun onRemoveFile(file: KmpFile)
    fun onSendClick(context: PlatformContext)
    fun onBackClick()
    fun onRemoveUrl(url: String)

    data class Model(
        val text: String = "",
        val files: List<KmpFile> = emptyList(),
        val attachedAiMediaUrls: List<String> = emptyList(),
        val isSending: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            initialImageUrl: String?,
            onPostCreated: (Post) -> Unit,
            onBack: () -> Unit
        ): CreatePostComponent
    }
}