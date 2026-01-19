package org.example.whiskr.component.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

interface HomeComponent {
    val model: Value<Model>

    val createPostComponent: CreatePostComponent

    fun onRefresh()
    fun onLoadMore()
    fun onLikeClick(postId: Long)
    fun onNavigateToCreatePostScreen()
    fun onProfileClick(userId: Long)
    fun onMediaClick(media: List<PostMedia>, index: Int)
    fun onCommentsClick(post: Post)

    data class Model(
        val items: List<Post> = emptyList(),
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
        val isEndOfList: Boolean = false,
        val currentPage: Int
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNavigateToCreatePost: () -> Unit,
            onNavigateToProfile: (Long) -> Unit,
            onNavigateToComments: (Post) -> Unit,
            onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit
        ): HomeComponent
    }
}