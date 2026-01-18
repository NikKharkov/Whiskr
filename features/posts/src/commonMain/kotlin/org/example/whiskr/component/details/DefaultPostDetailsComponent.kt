package org.example.whiskr.component.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

@Inject
class DefaultPostDetailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted val post: Post,
    @Assisted private val onNavigateToReply: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onBack: () -> Unit,
    private val postRepository: PostRepository
) : PostDetailsComponent, ComponentContext by componentContext {

    private val _model = MutableValue(PostDetailsComponent.Model(post))
    override val model: Value<PostDetailsComponent.Model> = _model
    private val scope = componentScope()

    init {
        loadReplies()
        observeNewReplies()
    }

    private fun loadReplies() {
        scope.launch {
            _model.update { it.copy(isLoadingReplies = true) }
            postRepository.getReplies(postId = post.id, page = 0)
                .onSuccess { paged ->
                    _model.update { it.copy(isLoadingReplies = false, replies = paged.content) }
                }
                .onFailure {
                    _model.update { it.copy(isLoadingReplies = false) }
                }
        }
    }

    private fun observeNewReplies() {
        scope.launch {
            postRepository.newPost.collect { newPost ->
                _model.update { state ->
                    if (newPost.parentPost?.id == state.post.id) {

                        val updatedReplies = if (state.replies.none { it.id == newPost.id }) {
                            listOf(newPost) + state.replies
                        } else {
                            state.replies
                        }

                        val updatedPost = state.post.copy(
                            stats = state.post.stats.copy(repliesCount = state.post.stats.repliesCount + 1)
                        )

                        state.copy(
                            post = updatedPost,
                            replies = updatedReplies
                        )
                    } else {
                        state
                    }
                }
            }
        }
    }

    override fun onLikeClick(postId: Long) {
        updatePost(postId) { post ->
            val newLiked = !post.interaction.isLiked
            val newCount = post.stats.likesCount + (if (newLiked) 1 else -1)

            val updatedPost = post.copy(
                interaction = post.interaction.copy(isLiked = newLiked),
                stats = post.stats.copy(likesCount = newCount)
            )

            scope.launch {
                postRepository.notifyPostUpdated(updatedPost)
            }

            updatedPost
        }

        scope.launch {
            postRepository.toggleLike(postId).onFailure {
                updatePost(postId) { post ->
                    post
                }
            }
        }
    }

    private fun updatePost(postId: Long, transform: (Post) -> Post) {
        _model.update { stats ->
            val newPost = if (stats.post.id == postId) {
                transform(stats.post)
            } else {
                stats.post
            }

            val newReplies = stats.replies.map {
                if (it.id == postId) transform(it) else it
            }

            stats.copy(
                post = newPost,
                replies = newReplies
            )
        }
    }

    override fun onBackClick() = onBack()
    override fun onReplyClick(post: Post) = onNavigateToReply(post)
    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
}