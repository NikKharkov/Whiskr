package org.example.whiskr.component.hashtags

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.PagingDelegate
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

interface HashtagsComponent {
    val model: Value<Model>
    val hashtag: String

    fun onBackClick()
    fun onLoadMore()
    fun onLikeClick(postId: Long)
    fun onCommentsClick(post: Post)
    fun onShareClick(post: Post)
    fun onMediaClick(media: List<PostMedia>, index: Int)
    fun onHashtagClick(tag: String)

    data class Model(
        val listState: PagingDelegate.State<Post>
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            hashtag: String,
            onBack: () -> Unit,
            onNavigateToComments: (Post) -> Unit,
            onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
            onNavigateToHashtag: (String) -> Unit
        ): HashtagsComponent
    }
}