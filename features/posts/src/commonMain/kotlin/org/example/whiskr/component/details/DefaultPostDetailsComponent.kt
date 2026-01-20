package org.example.whiskr.component.details

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.PagingDelegate
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

    private val scope = componentScope()

    private val headerPost = MutableValue(post)

    private val pagingDelegate = PagingDelegate(
        scope = scope,
        loader = { page -> postRepository.getReplies(postId = post.id, page = page) }
    )

    override val model: Value<PostDetailsComponent.Model> = combine(
        headerPost,
        pagingDelegate.state
    ) { header, listState ->
        PostDetailsComponent.Model(
            post = header,
            listState = listState
        )
    }

    init {
        pagingDelegate.firstLoad()
        observePostUpdates()
    }

    override fun onLoadMore() = pagingDelegate.loadMore()

    private fun observePostUpdates() {
        scope.launch {
            merge(
                postRepository.newPost.map { RepoEvent.NewPost(it) },
                postRepository.postUpdated.map { RepoEvent.PostUpdated(it) }
            ).collect { event ->
                when (event) {
                    is RepoEvent.NewPost -> handleNewPost(event.post)
                    is RepoEvent.PostUpdated -> handlePostUpdate(event.post)
                }
            }
        }
    }

    private fun handleNewPost(newPost: Post) {
        val currentHeader = headerPost.value

        if (newPost.parentPost?.id != currentHeader.id) return

        val currentReplies = pagingDelegate.state.value.items
        if (currentReplies.any { it.id == newPost.id }) return

        val updatedHeader = currentHeader.copy(
            stats = currentHeader.stats.copy(repliesCount = currentHeader.stats.repliesCount + 1)
        )

        headerPost.value = updatedHeader
        pagingDelegate.prependItem(newPost)

        scope.launch { postRepository.notifyPostUpdated(updatedHeader) }
    }

    private fun handlePostUpdate(updatedPost: Post) {
        if (headerPost.value.id == updatedPost.id) {
            headerPost.value = updatedPost
        }

        pagingDelegate.updateItems { list ->
            list.map { if (it.id == updatedPost.id) updatedPost else it }
        }
    }

    override fun onLikeClick(postId: Long) {
        val currentHeader = headerPost.value
        val currentReplies = pagingDelegate.state.value.items

        val targetPost = if (currentHeader.id == postId) {
            currentHeader
        } else {
            currentReplies.find { it.id == postId }
        } ?: return

        val newLiked = !targetPost.interaction.isLiked
        val newCount = targetPost.stats.likesCount + (if (newLiked) 1 else -1)

        val updatedPost = targetPost.copy(
            interaction = targetPost.interaction.copy(isLiked = newLiked),
            stats = targetPost.stats.copy(likesCount = newCount)
        )

        if (currentHeader.id == postId) {
            headerPost.value = updatedPost
        } else {
            pagingDelegate.updateItems { list ->
                list.map { if (it.id == postId) updatedPost else it }
            }
        }

        scope.launch {
            postRepository.notifyPostUpdated(updatedPost)
            postRepository.toggleLike(postId)
        }
    }

    override fun onBackClick() = onBack()
    override fun onReplyClick(post: Post) = onNavigateToReply(post)
    override fun onPostClick(post: Post) = onNavigateToPostDetails(post)
    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)

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

                return Cancellation {
                    c1.cancel()
                    c2.cancel()
                }
            }
        }
    }
}