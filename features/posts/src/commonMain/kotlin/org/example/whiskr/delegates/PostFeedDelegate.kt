package org.example.whiskr.delegates

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import org.example.whiskr.PagingDelegate
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.domain.RepoEvent
import org.example.whiskr.domain.ShareService
import org.example.whiskr.dto.PagedResponse
import org.example.whiskr.dto.Post

class PostFeedDelegate(
    private val scope: CoroutineScope,
    private val postRepository: PostRepository,
    private val shareService: ShareService,
    private val loader: suspend (page: Int) -> Result<PagedResponse<Post>>,
    private val onNewPost: (Post) -> Unit = {}
) {
    private val pagingDelegate = PagingDelegate(
        scope = scope,
        loader = loader
    )

    val state: Value<PagingDelegate.State<Post>> = pagingDelegate.state

    init {
        pagingDelegate.firstLoad()
        observePostUpdates()
    }

    fun onRefresh() = pagingDelegate.refresh()
    fun onLoadMore() = pagingDelegate.loadMore()

    private fun observePostUpdates() {
        scope.launch {
            merge(
                postRepository.newPost.map { RepoEvent.NewPost(it) },
                postRepository.postUpdated.map { RepoEvent.PostUpdated(it) }
            ).collect { event ->
                when (event) {
                    is RepoEvent.NewPost -> {
                        onNewPost(event.post)
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

    fun onLikeClick(postId: Long) {
        val currentItems = state.value.items
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

    fun onShareClick(post: Post) {
        val link = "whiskr://app/post/${post.id}"
        val text = "Check out this post by ${post.author.displayName}:\n$link"
        shareService.share(text)
    }

    fun prependItem(post: Post) = pagingDelegate.prependItem(post)
}