package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.component.create.DefaultCreatePostComponent
import org.example.whiskr.component.details.DefaultPostDetailsComponent
import org.example.whiskr.component.details.PostDetailsComponent
import org.example.whiskr.component.hashtags.DefaultHashtagsComponent
import org.example.whiskr.component.hashtags.HashtagsComponent
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

interface PostModule {

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
        factory: (ComponentContext, String?, (Post) -> Unit, () -> Unit) -> DefaultCreatePostComponent
    ): CreatePostComponent.Factory {
        return CreatePostComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideHomeFactory(
        factory: (ComponentContext, () -> Unit, (Long) -> Unit, (Post) -> Unit, (List<PostMedia>, Int) -> Unit, (String) -> Unit) -> DefaultHomeComponent
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
        factory: (ComponentContext, Long, (Post) -> Unit, (Post) -> Unit, (List<PostMedia>, Int) -> Unit, () -> Unit, (String) -> Unit) -> DefaultPostDetailsComponent
    ): PostDetailsComponent.Factory {
        return PostDetailsComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideHashtagsFactory(
        factory: (ComponentContext, String, () -> Unit, (Post) -> Unit, (List<PostMedia>, Int) -> Unit, (String) -> Unit) -> DefaultHashtagsComponent
    ): HashtagsComponent.Factory {
        return HashtagsComponent.Factory(factory)
    }
}