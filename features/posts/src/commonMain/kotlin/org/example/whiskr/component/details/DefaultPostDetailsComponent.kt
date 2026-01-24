package org.example.whiskr.component.details

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope
import org.example.whiskr.delegates.PostFeedDelegate
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.domain.ShareService
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

@Inject
class DefaultPostDetailsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val postId: Long,
    @Assisted private val onNavigateToReply: (Post) -> Unit,
    @Assisted private val onNavigateToPostDetails: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    private val postRepository: PostRepository,
    private val shareService: ShareService
) : PostDetailsComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private data class HeaderState(
        val post: Post? = null,
        val isLoading: Boolean = true,
        val isError: Boolean = false
    )
    private val headerState = MutableValue(HeaderState())

    private val feedDelegate: PostFeedDelegate by lazy {
        PostFeedDelegate(
            scope = scope,
            postRepository = postRepository,
            shareService = shareService,
            loader = { page -> postRepository.getReplies(postId = postId, page = page) },
            onNewPost = { newPost -> handleNewReply(newPost) }
        )
    }

    override val model: Value<PostDetailsComponent.Model> = combine(
        headerState,
        feedDelegate.state
    ) { header, listState ->
        PostDetailsComponent.Model(
            post = header.post,
            isLoadingPost = header.isLoading,
            isError = header.isError,
            listState = listState
        )
    }

    init {
        loadHeaderPost()
        observeHeaderUpdates()
    }

    private fun loadHeaderPost() {
        scope.launch {
            postRepository.getPostById(postId)
                .onSuccess { post ->
                    headerState.value = HeaderState(post = post, isLoading = false)
                }
                .onFailure {
                    headerState.value = HeaderState(isLoading = false, isError = true)
                }
        }
    }

    private fun observeHeaderUpdates() {
        scope.launch {
            postRepository.postUpdated.collect { updatedPost ->
                val currentHeader = headerState.value.post
                if (currentHeader?.id == updatedPost.id) {
                    headerState.value = headerState.value.copy(post = updatedPost)
                }
            }
        }
    }

    private fun handleNewReply(newPost: Post) {
        val currentHeader = headerState.value.post ?: return

        if (newPost.parentPost?.id != currentHeader.id) return

        val updatedHeader = currentHeader.copy(
            stats = currentHeader.stats.copy(repliesCount = currentHeader.stats.repliesCount + 1)
        )
        headerState.value = headerState.value.copy(post = updatedHeader)

        feedDelegate.prependItem(newPost)

        scope.launch { postRepository.notifyPostUpdated(updatedHeader) }
    }

    override fun onLikeClick(postId: Long) {
        val currentHeader = headerState.value.post

        if (currentHeader?.id == postId) {
            val newLiked = !currentHeader.interaction.isLiked
            val newCount = currentHeader.stats.likesCount + (if (newLiked) 1 else -1)

            val updatedPost = currentHeader.copy(
                interaction = currentHeader.interaction.copy(isLiked = newLiked),
                stats = currentHeader.stats.copy(likesCount = newCount)
            )

            headerState.value = headerState.value.copy(post = updatedPost)

            scope.launch {
                postRepository.notifyPostUpdated(updatedPost)
                postRepository.toggleLike(postId)
            }
        } else {
            feedDelegate.onLikeClick(postId)
        }
    }

    override fun onShareClick(post: Post) {
        feedDelegate.onShareClick(post)
    }

    override fun onLoadMore() = feedDelegate.onLoadMore()

    override fun onBackClick() = onBack()
    override fun onReplyClick(post: Post) = onNavigateToReply(post)
    override fun onCommentsClick(post: Post) = onNavigateToPostDetails(post)
    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onHashtagClick(tag: String) = onNavigateToHashtag(tag)

    private fun <T1 : Any, T2 : Any, R : Any> combine(
        value1: Value<T1>,
        value2: Value<T2>,
        transform: (T1, T2) -> R
    ): Value<R> {
        return object : Value<R>() {
            override val value: R
                get() = transform(value1.value, value2.value)

            override fun subscribe(observer: (R) -> Unit): Cancellation {
                val callback = { _: Any? -> observer(value) }
                val c1 = value1.subscribe(callback)
                val c2 = value2.subscribe(callback)
                return Cancellation { c1.cancel(); c2.cancel() }
            }
        }
    }
}