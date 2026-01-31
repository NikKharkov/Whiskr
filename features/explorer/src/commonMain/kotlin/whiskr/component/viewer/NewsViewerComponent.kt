package org.example.whiskr.component.viewer

import com.arkivanov.decompose.ComponentContext

interface NewsViewerComponent {

    val url: String

    fun onBackClick()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            url: String,
            onBack: () -> Unit
        ): NewsViewerComponent
    }
}