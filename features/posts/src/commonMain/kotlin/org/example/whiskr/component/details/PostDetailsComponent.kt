package org.example.whiskr.component.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.PagingDelegate
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

interface PostDetailsComponent {
    val model: Value<Model>

    fun onBackClick()
    fun onLoadMore()
    fun onReplyClick(post: Post)
    fun onPostClick(post: Post)
    fun onLikeClick(postId: Long)
    fun onMediaClick(media: List<PostMedia>, index: Int)
    fun onShareClick(post: Post)

    data class Model(
        val post: Post?,
        val isLoadingPost: Boolean,
        val isError: Boolean,
        val listState: PagingDelegate.State<Post>
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            postId: Long,
            onNavigateToReply: (Post) -> Unit,
            onNavigateToPostDetails: (Post) -> Unit,
            onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
            onBack: () -> Unit
        ): PostDetailsComponent
    }
}