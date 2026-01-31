package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.explore.DefaultExploreComponent
import org.example.whiskr.component.explore.ExploreComponent
import org.example.whiskr.component.viewer.DefaultNewsViewerComponent
import org.example.whiskr.component.viewer.NewsViewerComponent
import org.example.whiskr.data.ExplorerApiService
import org.example.whiskr.data.ExplorerRepositoryImpl
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.domain.ExplorerRepository

interface ExploreModule {

    @Provides
    @Singleton
    fun provideSearchApi(ktorfit: Ktorfit): ExplorerApiService = ktorfit.create()

    @Provides
    @Singleton
    fun provideSearchRepository(explorerApiService: ExplorerApiService): ExplorerRepository {
        return ExplorerRepositoryImpl(explorerApiService)
    }

    @Provides
    @Singleton
    fun provideExplorerFactory(
        factory: (
            ComponentContext,
            (Post) -> Unit,   // onPost
            (String) -> Unit, // onProfile
            (List<PostMedia>, Int) -> Unit, // onMedia
            (String) -> Unit, // onHashtag
            (String) -> Unit, //onNews
            (Post) -> Unit // onRepost
        ) -> DefaultExploreComponent
    ): ExploreComponent.Factory {
        return ExploreComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideNewsViewerFactory(
        factory: (
            ComponentContext,
            String,
            () -> Unit
        ) -> DefaultNewsViewerComponent
    ): NewsViewerComponent.Factory {
        return NewsViewerComponent.Factory(factory)
    }

}