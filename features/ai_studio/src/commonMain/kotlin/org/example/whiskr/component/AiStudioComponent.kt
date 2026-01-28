package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.data.AiArtStyle
import org.example.whiskr.data.AiGalleryItemDto

interface AiStudioComponent {

    val model: Value<Model>

    fun onPromptChanged(text: String)
    fun onStyleSelected(style: AiArtStyle?)
    fun onSourceImagePicked(bytes: ByteArray?)
    fun onGenerateClicked()
    fun onGalleryLoadMore()

    fun onImageClicked(url: String)
    fun onPostClicked(url: String)
    fun onShareClicked(url: String)
    fun onDownloadClicked(url: String)

    data class Model(
        val balance: Long,
        val prompt: String = "",
        val selectedStyle: AiArtStyle? = null,
        val sourceImageBytes: ByteArray? = null,
        val isGenerating: Boolean = false,
        val error: String? = null,
        val galleryItems: List<AiGalleryItemDto> = emptyList(),
        val isGalleryLoading: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            initialBalance: Long,
            onNavigateToMediaViewer: (url: String) -> Unit,
            onNavigateToCreatePost: (url: String) -> Unit
        ): AiStudioComponent
    }
}