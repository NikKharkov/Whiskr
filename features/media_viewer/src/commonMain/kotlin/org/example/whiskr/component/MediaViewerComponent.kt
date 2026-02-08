package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import org.example.whiskr.data.MediaType
import org.example.whiskr.data.Media

interface MediaViewerComponent {

    val mediaList: List<Media>
    val initialIndex: Int

    fun onBackClicked()
    fun onDownloadClicked(url: String, type: MediaType)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            mediaList: List<Media>,
            initialIndex: Int,
            onFinished: () -> Unit
        ): MediaViewerComponent
    }
}