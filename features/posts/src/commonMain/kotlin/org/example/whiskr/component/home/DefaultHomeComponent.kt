package org.example.whiskr.component.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
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

    private val _model = MutableValue(HomeComponent.Model(currentPage = 0))
    override val model: Value<HomeComponent.Model> = _model

    override val createPostComponent: CreatePostComponent = createPostFactory(
        componentContext = childContext("EmbeddedCreatePost"),
        onPostCreated = { newPost ->
            handleNewPost(newPost)
        },
        onBack = {}
    )

    init {
        loadFeed(isRefresh = true)
        observePostUpdates()
    }

    override fun onRefresh() {
        loadFeed(isRefresh = true)
    }

    override fun onLoadMore() {
        if (_model.value.isLoadingMore || _model.value.isEndOfList) return
        loadFeed(isRefresh = false)
    }

    private fun loadFeed(isRefresh: Boolean) {
        scope.launch {
            _model.update { state ->
                if (isRefresh) {
                    state.copy(isRefreshing = true, isError = false)
                } else {
                    state.copy(isLoadingMore = true, isError = false)
                }
            }

            val targetPage = if (isRefresh) 0 else _model.value.currentPage + 1

            postRepository.getFeed(targetPage)
                .onSuccess { page ->
                    _model.update { state ->
                        val newItems = if (isRefresh) page.content else state.items + page.content
                        state.copy(
                            items = newItems,
                            currentPage = targetPage,
                            isRefreshing = false,
                            isLoadingMore = false,
                            isLoading = false,
                            isEndOfList = page.last
                        )
                    }
                }
                .onFailure {
                    _model.update { state ->
                        state.copy(
                            isRefreshing = false,
                            isLoadingMore = false,
                            isLoading = false,
                            isError = true
                        )
                    }
                }
        }
    }

    private fun observePostUpdates() {
        scope.launch {
            merge(
                postRepository.newPost.map { RepoEvent.NewPost(it) },
                postRepository.postUpdated.map { RepoEvent.PostUpdated(it) }
            ).collect { event ->
                when (event) {
                    is RepoEvent.NewPost -> handleNewPost(event.post)
                    is RepoEvent.PostUpdated -> handlePostUpdated(event.post)
                }
            }
        }
    }

    private fun handleNewPost(newPost: Post) {
        _model.update { state ->
            if (newPost.parentPost != null) return@update state
            if (state.items.any { it.id == newPost.id }) return@update state

            state.copy(items = listOf(newPost) + state.items)
        }
    }

    private fun handlePostUpdated(updatedPost: Post) {
        _model.update { state ->
            val index = state.items.indexOfFirst { it.id == updatedPost.id }
            if (index == -1) return@update state

            val newItems = state.items.toMutableList().apply {
                set(index, updatedPost)
            }
            state.copy(items = newItems)
        }
    }

    override fun onLikeClick(postId: Long) {
        var postToNotify: Post? = null

        _model.update { state ->
            val index = state.items.indexOfFirst { it.id == postId }
            if (index == -1) return@update state

            val post = state.items[index]
            val newLiked = !post.interaction.isLiked
            val newCount = post.stats.likesCount + (if (newLiked) 1 else -1)

            val updatedPost = post.copy(
                interaction = post.interaction.copy(isLiked = newLiked),
                stats = post.stats.copy(likesCount = newCount)
            )

            postToNotify = updatedPost

            val newItems = state.items.toMutableList().apply { set(index, updatedPost) }
            state.copy(items = newItems)
        }

        postToNotify?.let { updatedPost ->
            scope.launch {
                postRepository.notifyPostUpdated(updatedPost)
                postRepository.toggleLike(postId)
            }
        }
    }

    override fun onNavigateToCreatePostScreen() = onNavigateToCreatePost()
    override fun onProfileClick(userId: Long) = onNavigateToProfile(userId)
    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onCommentsClick(post: Post) = onNavigateToComments(post)
}