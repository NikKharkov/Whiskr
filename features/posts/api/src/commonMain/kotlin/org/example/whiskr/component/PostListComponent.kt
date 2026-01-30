package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.PagingDelegate
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.dto.PagedResponse

interface PostListComponent {
    val model: Value<Model>

    fun onRefresh()
    fun onLoadMore()
    fun onLikeClick(postId: Long)
    fun onShareClick(post: Post)
    fun onRepostClick(post: Post)
    fun onMediaClick(media: List<PostMedia>, index: Int)
    fun onCommentsClick(post: Post)
    fun onProfileClick(handle: String)
    fun onHashtagClick(tag: String)
    fun insertNewPost(post: Post)

    data class Model(val listState: PagingDelegate.State<Post>)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            loader: suspend (page: Int) -> Result<PagedResponse<Post>>,
            onNavigateToProfile: (String) -> Unit,
            onNavigateToComments: (Post) -> Unit,
            onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
            onNavigateToHashtag: (String) -> Unit,
            onNavigateToRepost: (Post) -> Unit
        ): PostListComponent
    }
}