package org.example.whiskr.component.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.PostDetailsComponent
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.component.componentScope
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.domain.PostRepository

@Inject
class DefaultPostDetailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val postId: Long,
    @Assisted private val onNavigateToReply: (Post) -> Unit,
    @Assisted private val onNavigateToPostDetails: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    @Assisted private val onNavigateToProfile: (String) -> Unit,
    postListFactory: PostListComponent.Factory,
    private val postRepository: PostRepository
) : PostDetailsComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableValue(PostDetailsComponent.Model())
    override val model: Value<PostDetailsComponent.Model> = _model

    override val repliesComponent: PostListComponent = postListFactory(
        componentContext = childContext("RepliesList"),
        loader = { page -> postRepository.getReplies(postId = postId, page = page) },
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToComments = onNavigateToPostDetails,
        onNavigateToMediaViewer = onNavigateToMediaViewer,
        onNavigateToHashtag = onNavigateToHashtag
    )

    init {
        loadHeaderPost()
        observeHeaderUpdates()
        observeNewReplies()
    }

    private fun loadHeaderPost() {
        scope.launch {
            postRepository.getPostById(postId)
                .onSuccess { post ->
                    _model.value = _model.value.copy(post = post, isLoadingPost = false)
                }
                .onFailure {
                    _model.value = _model.value.copy(isLoadingPost = false, isError = true)
                }
        }
    }

    override fun onLikeClick(postId: Long) {
        val currentPost = _model.value.post ?: return
        if (currentPost.id != postId) return

        val newLiked = !currentPost.interaction.isLiked
        val newCount = currentPost.stats.likesCount + (if (newLiked) 1 else -1)

        val updatedPost = currentPost.copy(
            interaction = currentPost.interaction.copy(isLiked = newLiked),
            stats = currentPost.stats.copy(likesCount = newCount)
        )
        _model.value = _model.value.copy(post = updatedPost)

        scope.launch {
            postRepository.notifyPostUpdated(updatedPost)
            postRepository.toggleLike(postId)
        }
    }

    private fun observeHeaderUpdates() {
        scope.launch {
            postRepository.postUpdated.collect { updatedPost ->
                val currentPost = _model.value.post
                if (currentPost?.id == updatedPost.id) {
                    _model.value = _model.value.copy(post = updatedPost)
                }
            }
        }
    }

    private fun observeNewReplies() {
        scope.launch {
            postRepository.newPost.collect { newPost ->

                val currentHeader = _model.value.post ?: return@collect

                if (newPost.parentPost?.id == currentHeader.id) {

                    val updatedHeader = currentHeader.copy(
                        stats = currentHeader.stats.copy(
                            repliesCount = currentHeader.stats.repliesCount + 1
                        )
                    )
                    _model.value = _model.value.copy(post = updatedHeader)

                    repliesComponent.insertNewPost(newPost)

                    postRepository.notifyPostUpdated(updatedHeader)
                }
            }
        }
    }

    override fun onShareClick(post: Post) = repliesComponent.onShareClick(post)

    override fun onBackClick() = onBack()
    override fun onReplyClick(post: Post) = onNavigateToReply(post)
    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onNavigateToParentProfile(handle: String) = onNavigateToProfile(handle)
}