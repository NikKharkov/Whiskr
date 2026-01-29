package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.DefaultHomeComponent
import org.example.whiskr.component.HomeComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia

interface HomeModule {
    @Provides
    @Singleton
    fun provideHomeFactory(
        factory: (
            ComponentContext,
            () -> Unit, // onNavigateToCreatePost
            (Long) -> Unit, // onNavigateToProfile
            (Post) -> Unit, // onNavigateToComments
            (List<PostMedia>, Int) -> Unit, // onNavigateToMediaViewer
            (String) -> Unit // onNavigateToHashtag
        ) -> DefaultHomeComponent
    ): HomeComponent.Factory {
        return HomeComponent.Factory { context, navCreate, navProfile, navComments, navMedia, navHashtag ->
            factory(
                context,
                navCreate,
                navProfile,
                navComments,
                navMedia,
                navHashtag
            )
        }
    }
}