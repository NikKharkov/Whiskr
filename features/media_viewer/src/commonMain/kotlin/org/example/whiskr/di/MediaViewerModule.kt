package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.DefaultMediaViewerComponent
import org.example.whiskr.component.MediaViewerComponent
import org.example.whiskr.data.Media

interface MediaViewerModule {

    @Provides
    @Singleton
    fun provideMediaViewerFactory(
        factory: (ComponentContext, List<Media>, Int, () -> Unit) -> DefaultMediaViewerComponent
    ): MediaViewerComponent.Factory {
        return MediaViewerComponent.Factory(factory)
    }
}