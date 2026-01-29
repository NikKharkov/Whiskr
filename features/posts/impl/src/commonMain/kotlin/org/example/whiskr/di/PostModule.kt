package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.CreatePostComponent
import org.example.whiskr.component.CreateReplyComponent
import org.example.whiskr.component.HashtagsComponent
import org.example.whiskr.component.PostDetailsComponent
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.component.create.DefaultCreatePostComponent
import org.example.whiskr.component.details.DefaultPostDetailsComponent
import org.example.whiskr.component.hashtags.DefaultHashtagsComponent
import org.example.whiskr.component.list.DefaultPostListComponent
import org.example.whiskr.component.reply.DefaultCreateReplyComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostApiService
import org.example.whiskr.data.PostMedia
import org.example.whiskr.data.PostRepositoryImpl
import org.example.whiskr.data.createPostApiService
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.dto.PagedResponse

interface PostModule {

    @Provides
    @Singleton
    fun providePostApiService(ktorfit: Ktorfit): PostApiService = ktorfit.createPostApiService()

    @Provides
    @Singleton
    fun providePostRepository(
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
    fun providePostListFactory(
        factory: (
            ComponentContext,
            suspend (Int) -> Result<PagedResponse<Post>>,
            (String) -> Unit,
            (Post) -> Unit,
            (List<PostMedia>, Int) -> Unit,
            (String) -> Unit
        ) -> DefaultPostListComponent
    ): PostListComponent.Factory {
        return PostListComponent.Factory(factory)
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
        factory: (
            ComponentContext,
            Long,
            (Post) -> Unit,
            (Post) -> Unit,
            (List<PostMedia>, Int) -> Unit,
            () -> Unit,
            (String) -> Unit,
            (String) -> Unit
        ) -> DefaultPostDetailsComponent
    ): PostDetailsComponent.Factory {
        return PostDetailsComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideHashtagsFactory(
        factory: (
            ComponentContext,
            String, // hashtag
            () -> Unit, // onBack
            (Post) -> Unit, // comments
            (List<PostMedia>, Int) -> Unit, // media
            (String) -> Unit, // hashtag nav
            (String) -> Unit // profile nav
        ) -> DefaultHashtagsComponent
    ): HashtagsComponent.Factory {
        return HashtagsComponent.Factory(factory)
    }
}