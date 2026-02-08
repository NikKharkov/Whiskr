package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.DefaultHomeComponent
import org.example.whiskr.component.HomeComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.Media

interface HomeModule {
    @Provides
    @Singleton
    fun provideHomeFactory(
        factory: (
            ComponentContext,
            () -> Unit, // onNavigateToCreatePost
            (String) -> Unit, // onNavigateToProfile
            (Post) -> Unit, // onNavigateToComments
            (List<Media>, Int) -> Unit, // onNavigateToMediaViewer
            (String) -> Unit, // onNavigateToHashtag
            (Post) -> Unit // onNavigateToRepost
        ) -> DefaultHomeComponent
    ): HomeComponent.Factory {
        return HomeComponent.Factory(factory)
    }
}