package org.example.whiskr.component.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.domain.RepoEvent
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

@Inject
class DefaultPostDetailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted val post: Post,
    @Assisted private val onNavigateToReply: (Post) -> Unit,
    @Assisted private val onNavigateToPostDetails: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onBack: () -> Unit,
    private val postRepository: PostRepository
) : PostDetailsComponent, ComponentContext by componentContext {

    private val _model = MutableValue(PostDetailsComponent.Model(post = post, currentPage = 0))
    override val model: Value<PostDetailsComponent.Model> = _model
    private val scope = componentScope()

    init {
        loadReplies()
        observePostUpdates()
    }

    private fun loadReplies() {
        scope.launch {
            _model.update { it.copy(isLoading = true) }
            postRepository.getReplies(postId = post.id, page = 0)
                .onSuccess { paged ->
                    _model.update { it.copy(isLoading = false, replies = paged.content) }
                }
                .onFailure {
                    _model.update { it.copy(isLoading = false) }
                }
        }
    }

    override fun onLoadMore() {
        val state = _model.value
        if (state.isLoadingMore || state.isEndOfList) return

        scope.launch {
            _model.update { it.copy(isLoadingMore = true) }

            val nextPage = state.currentPage + 1

            postRepository.getReplies(postId = post.id, page = nextPage)
                .onSuccess { paged ->
                    _model.update {
                        it.copy(
                            replies = it.replies + paged.content,
                            isLoadingMore = false,
                            isEndOfList = paged.last,
                            currentPage = nextPage
                        )
                    }
                }
                .onFailure {
                    _model.update { it.copy(isLoadingMore = false) }
                }
        }
    }

    private fun observePostUpdates() {
        scope.launch {
            merge(
                postRepository.newPost.map { RepoEvent.NewPost(it) },
                postRepository.postUpdated.map { RepoEvent.PostUpdated(it) }
            ).collect { event ->
                _model.update { state ->
                    when (event) {
                        is RepoEvent.NewPost -> handleNewPost(state, event.post)
                        is RepoEvent.PostUpdated -> handlePostUpdate(state, event.post)
                    }
                }
            }
        }
    }

    private fun handleNewPost(
        state: PostDetailsComponent.Model,
        newPost: Post
    ): PostDetailsComponent.Model {
        val parentId = newPost.parentPost?.id
        if (parentId != state.post.id) return state

        if (state.replies.any { it.id == newPost.id }) return state

        val updatedPost = state.post.copy(
            stats = state.post.stats.copy(repliesCount = state.post.stats.repliesCount + 1)
        )

        scope.launch { postRepository.notifyPostUpdated(updatedPost) }

        return state.copy(
            post = updatedPost,
            replies = listOf(newPost) + state.replies
        )
    }

    private fun handlePostUpdate(
        state: PostDetailsComponent.Model,
        updatedPost: Post
    ): PostDetailsComponent.Model {
        val newPost = if (state.post.id == updatedPost.id) updatedPost else state.post
        val newReplies = state.replies.map { if (it.id == updatedPost.id) updatedPost else it }

        return state.copy(
            post = newPost,
            replies = newReplies
        )
    }

    override fun onLikeClick(postId: Long) {
        var postToNotify: Post? = null

        _model.update { state ->
            val targetPost =
                if (state.post.id == postId) state.post else state.replies.find { it.id == postId }

            if (targetPost == null) return@update state

            val newLiked = !targetPost.interaction.isLiked
            val newCount = targetPost.stats.likesCount + (if (newLiked) 1 else -1)

            val updatedPost = targetPost.copy(
                interaction = targetPost.interaction.copy(isLiked = newLiked),
                stats = targetPost.stats.copy(likesCount = newCount)
            )

            postToNotify = updatedPost

            if (state.post.id == postId) {
                state.copy(post = updatedPost)
            } else {
                state.copy(replies = state.replies.map { if (it.id == postId) updatedPost else it })
            }
        }

        postToNotify?.let { updatedPost ->
            scope.launch {
                postRepository.notifyPostUpdated(updatedPost)
                postRepository.toggleLike(postId)
            }
        }
    }

    override fun onBackClick() = onBack()
    override fun onReplyClick(post: Post) = onNavigateToReply(post)
    override fun onPostClick(post: Post) = onNavigateToPostDetails(post)
    override fun onMediaClick(media: List<PostMedia>, index: Int) =
        onNavigateToMediaViewer(media, index)
}