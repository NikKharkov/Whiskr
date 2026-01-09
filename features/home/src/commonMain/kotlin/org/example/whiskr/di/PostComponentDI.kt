package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.DefaultHomeComponent
import org.example.whiskr.component.HomeComponent
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.component.create.DefaultCreatePostComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostApiService
import org.example.whiskr.data.PostMedia
import org.example.whiskr.data.PostRepositoryImpl
import org.example.whiskr.data.createPostApiService
import org.example.whiskr.domain.MediaProcessingService
import org.example.whiskr.domain.PostRepository

interface PostComponentDI {

    @Provides
    @Singleton
    fun provideHomeApiService(ktorfit: Ktorfit): PostApiService = ktorfit.createPostApiService()

    @Provides
    @Singleton
    fun provideHomeRepository(
        postApiService: PostApiService,
        mediaProcessingService: MediaProcessingService
    ): PostRepository =
        PostRepositoryImpl(postApiService, mediaProcessingService)


    @Provides
    @Singleton
    fun provideCreatePostFactory(
        factory: (ComponentContext, (Post) -> Unit, () -> Unit) -> DefaultCreatePostComponent
    ): CreatePostComponent.Factory {
        return CreatePostComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun providePostFactory(
        factory: (ComponentContext, () -> Unit, (Long) -> Unit, (PostMedia) -> Unit) -> DefaultHomeComponent
    ): HomeComponent.Factory {
        return HomeComponent.Factory(factory)
    }
}