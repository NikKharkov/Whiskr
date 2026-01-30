package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia

interface PostDetailsComponent {
    val model: Value<Model>

    val repliesComponent: PostListComponent

    fun onBackClick()
    fun onReplyClick(post: Post)
    fun onRepostClick(post: Post)
    fun onLikeClick(postId: Long)
    fun onShareClick(post: Post)
    fun onMediaClick(media: List<PostMedia>, index: Int)
    fun onNavigateToParentProfile(handle: String)

    data class Model(
        val post: Post? = null,
        val isLoadingPost: Boolean = true,
        val isError: Boolean = false,
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            postId: Long,
            onNavigateToReply: (Post) -> Unit,
            onNavigateToPostDetails: (Post) -> Unit,
            onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
            onBack: () -> Unit,
            onNavigateToHashtag: (String) -> Unit,
            onNavigateToProfile: (String) -> Unit,
            onNavigateToRepost: (Post) -> Unit
        ): PostDetailsComponent
    }
}