package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.component.create.DefaultCreatePostComponent
import org.example.whiskr.component.details.DefaultPostDetailsComponent
import org.example.whiskr.component.details.PostDetailsComponent
import org.example.whiskr.component.home.DefaultHomeComponent
import org.example.whiskr.component.home.HomeComponent
import org.example.whiskr.component.reply.CreateReplyComponent
import org.example.whiskr.component.reply.DefaultCreateReplyComponent
import org.example.whiskr.data.PostApiService
import org.example.whiskr.data.PostRepositoryImpl
import org.example.whiskr.data.createPostApiService
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

interface PostComponentDI {

    @Provides
    @Singleton
    fun provideHomeApiService(ktorfit: Ktorfit): PostApiService = ktorfit.createPostApiService()

    @Provides
    @Singleton
    fun provideHomeRepository(
        postApiService: PostApiService
    ): PostRepository = PostRepositoryImpl(postApiService)

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
        factory: (ComponentContext, () -> Unit, (Long) -> Unit, (Post) -> Unit, (List<PostMedia>, Int) -> Unit) -> DefaultHomeComponent
    ): HomeComponent.Factory {
        return HomeComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideCreateReplyFactory(
        factory: (ComponentContext, Post, (Post) -> Unit, () -> Unit) -> DefaultCreateReplyComponent
    ): CreateReplyComponent.Factory {
        return CreateReplyComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun providePostDetailsFactory(
        factory: (ComponentContext, Post, (Post) -> Unit, (Post) -> Unit, (List<PostMedia>, Int) -> Unit, () -> Unit) -> DefaultPostDetailsComponent
    ): PostDetailsComponent.Factory {
        return PostDetailsComponent.Factory(factory)
    }
}