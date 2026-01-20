package org.example.whiskr.component.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.PagingDelegate
import org.example.whiskr.component.componentScope
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.domain.RepoEvent
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

@Inject
class DefaultHomeComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNavigateToCreatePost: () -> Unit,
    @Assisted private val onNavigateToProfile: (Long) -> Unit,
    @Assisted private val onNavigateToComments: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    private val postRepository: PostRepository,
    private val createPostFactory: CreatePostComponent.Factory
) : HomeComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val pagingDelegate = PagingDelegate(
        scope = scope,
        loader = { page -> postRepository.getFeed(page) }
    )

    override val model: Value<HomeComponent.Model> = pagingDelegate.state.map { pagingState ->
        HomeComponent.Model(listState = pagingState)
    }

    override val createPostComponent: CreatePostComponent = createPostFactory(
        componentContext = childContext("EmbeddedCreatePost"),
        onPostCreated = { newPost ->
            if (newPost.parentPost == null) {
                pagingDelegate.prependItem(newPost)
            }
        },
        onBack = {}
    )

    init {
        pagingDelegate.firstLoad()
        observePostUpdates()
    }

    override fun onRefresh() = pagingDelegate.refresh()
    override fun onLoadMore() = pagingDelegate.loadMore()

    private fun observePostUpdates() {
        scope.launch {
            merge(
                postRepository.newPost.map { RepoEvent.NewPost(it) },
                postRepository.postUpdated.map { RepoEvent.PostUpdated(it) }
            ).collect { event ->
                when (event) {
                    is RepoEvent.NewPost -> {
                        if (event.post.parentPost == null) {
                            pagingDelegate.prependItem(event.post)
                        }
                    }
                    is RepoEvent.PostUpdated -> {
                        pagingDelegate.updateItems { list ->
                            list.map { if (it.id == event.post.id) event.post else it }
                        }
                    }
                }
            }
        }
    }

    override fun onLikeClick(postId: Long) {
        val currentItems = pagingDelegate.state.value.items
        val post = currentItems.find { it.id == postId } ?: return

        val newLiked = !post.interaction.isLiked
        val newCount = post.stats.likesCount + (if (newLiked) 1 else -1)

        val updatedPost = post.copy(
            interaction = post.interaction.copy(isLiked = newLiked),
            stats = post.stats.copy(likesCount = newCount)
        )

        pagingDelegate.updateItems { list ->
            list.map { if (it.id == postId) updatedPost else it }
        }

        scope.launch {
            postRepository.notifyPostUpdated(updatedPost)
            postRepository.toggleLike(postId)
        }
    }

    override fun onNavigateToCreatePostScreen() = onNavigateToCreatePost()
    override fun onProfileClick(userId: Long) = onNavigateToProfile(userId)
    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onCommentsClick(post: Post) = onNavigateToComments(post)
}