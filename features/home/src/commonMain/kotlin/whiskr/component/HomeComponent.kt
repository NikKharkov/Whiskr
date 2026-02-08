package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import org.example.whiskr.data.Post
import org.example.whiskr.dto.Media

interface HomeComponent {
    val postsComponent: PostListComponent
    val createPostComponent: CreatePostComponent

    fun onNavigateToCreatePostScreen()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNavigateToCreatePost: () -> Unit,
            onNavigateToProfile: (String) -> Unit,
            onNavigateToComments: (Post) -> Unit,
            onNavigateToMediaViewer: (List<Media>, Int) -> Unit,
            onNavigateToHashtag: (String) -> Unit,
            onNavigateToRepost: (Post) -> Unit
        ): HomeComponent
    }
}