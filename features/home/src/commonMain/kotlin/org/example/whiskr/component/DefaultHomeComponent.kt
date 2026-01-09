package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.domain.PostRepository

@Inject
class DefaultHomeComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNavigateToCreatePost: () -> Unit,
    @Assisted private val onNavigateToProfile: (Long) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (PostMedia) -> Unit,
    private val postRepository: PostRepository,
    private val createPostFactory: CreatePostComponent.Factory
) : HomeComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableValue(HomeComponent.Model())
    override val model: Value<HomeComponent.Model> = _model

    override val createPostComponent: CreatePostComponent = createPostFactory(
        componentContext = childContext("EmbeddedCreatePost"),
        onPostCreated = { newPost ->
            _model.update { it.copy(items = listOf(newPost) + it.items) }
        },
        onBack = {}
    )

    private var currentPage = 0

    init {
        loadFeed(isRefresh = true)
    }

    override fun onRefresh() {
        loadFeed(isRefresh = true)
    }

    private fun loadFeed(isRefresh: Boolean) {
        if (_model.value.isLoading && !isRefresh) return

        scope.launch {
            if (isRefresh) _model.update { it.copy(isRefreshing = true, isError = false) }

            postRepository.getFeed(0).onSuccess { page ->
                currentPage = 0
                _model.update {
                    it.copy(
                        items = page.content,
                        isRefreshing = false,
                        isLoading = false,
                        isEndOfList = page.last
                    )
                }
            }.onFailure {
                _model.update { it.copy(isRefreshing = false, isError = true, isLoading = false) }
            }
        }
    }

    override fun onLoadMore() {
        val state = _model.value
        if (state.isLoadingMore || state.isEndOfList) return

        scope.launch {
            _model.update { it.copy(isLoadingMore = true) }
            val nextPage = currentPage + 1

            postRepository.getFeed(nextPage).onSuccess { page ->
                currentPage = nextPage
                _model.update {
                    it.copy(
                        items = it.items + page.content,
                        isLoadingMore = false,
                        isEndOfList = page.last
                    )
                }
            }.onFailure {
                _model.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    override fun onLikeClick(postId: Long) {
        updatePostInList(postId) { post ->
            val newLiked = !post.interaction.isLiked
            val newCount = post.stats.likesCount + (if (newLiked) 1 else -1)
            post.copy(
                interaction = post.interaction.copy(isLiked = newLiked),
                stats = post.stats.copy(likesCount = newCount)
            )
        }

        scope.launch {
            postRepository.toggleLike(postId).onFailure {
                updatePostInList(postId) { post -> post }
            }
        }
    }

    private fun updatePostInList(postId: Long, transform: (Post) -> Post) {
        _model.update { s -> s.copy(items = s.items.map { if (it.id == postId) transform(it) else it }) }
    }

    override fun onNavigateToCreatePostScreen() = onNavigateToCreatePost()
    override fun onProfileClick(userId: Long) = onNavigateToProfile(userId)
    override fun onMediaClick(media: PostMedia) = onNavigateToMediaViewer(media)
}