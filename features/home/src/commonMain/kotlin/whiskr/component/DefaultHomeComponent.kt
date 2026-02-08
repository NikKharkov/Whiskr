package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.data.Post
import org.example.whiskr.data.Media
import org.example.whiskr.domain.PostRepository

@Inject
class DefaultHomeComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNavigateToCreatePost: () -> Unit,
    @Assisted private val onNavigateToProfile: (String) -> Unit,
    @Assisted private val onNavigateToComments: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<Media>, Int) -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    @Assisted private val onNavigateToRepost: (Post) -> Unit,
    private val postRepository: PostRepository,
    createPostFactory: CreatePostComponent.Factory,
    postListFactory: PostListComponent.Factory
) : HomeComponent, ComponentContext by componentContext {

    override val postsComponent: PostListComponent = postListFactory(
        componentContext = childContext("HomeFeed"),
        loader = { page -> postRepository.getFeed(page) },
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToComments = onNavigateToComments,
        onNavigateToMediaViewer = onNavigateToMediaViewer,
        onNavigateToHashtag = onNavigateToHashtag,
        onNavigateToRepost = onNavigateToRepost
    )

    override val createPostComponent: CreatePostComponent = createPostFactory(
        componentContext = childContext("HomeCreatePost"),
        initialImageUrl = null,
        onBack = {},
        onPostCreated = { newPost ->
            postsComponent.insertNewPost(newPost)
        }
    )

    override fun onNavigateToCreatePostScreen() = onNavigateToCreatePost()
}