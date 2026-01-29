package org.example.whiskr.component.hashtags

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.HashtagsComponent
import org.example.whiskr.component.componentScope
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.delegates.PostFeedDelegate
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.domain.ShareService

@Inject
class DefaultHashtagsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted override val hashtag: String,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onNavigateToComments: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    private val postRepository: PostRepository,
    private val shareService: ShareService
) : HashtagsComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val feedDelegate: PostFeedDelegate by lazy {
        PostFeedDelegate(
            scope = scope,
            postRepository = postRepository,
            shareService = shareService,
            loader = { page -> postRepository.getPostsByHashtag(hashtag, page) },
            onNewPost = { newPost ->
                if (newPost.hashtags.contains(hashtag)) {
                    feedDelegate.prependItem(newPost)
                }
            }
        )
    }

    override val model: Value<HashtagsComponent.Model> = feedDelegate.state.map {
        HashtagsComponent.Model(listState = it)
    }

    override fun onLoadMore() = feedDelegate.onLoadMore()
    override fun onLikeClick(postId: Long) = feedDelegate.onLikeClick(postId)
    override fun onShareClick(post: Post) = feedDelegate.onShareClick(post)

    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onCommentsClick(post: Post) = onNavigateToComments(post)
    override fun onBackClick() = onBack()
    override fun onHashtagClick(tag: String) = onNavigateToHashtag(tag)
}