package org.example.whiskr.component.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.operator.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.component.componentScope
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.delegates.PostFeedDelegate
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.domain.ShareService
import org.example.whiskr.dto.PagedResponse

@Inject
class DefaultPostListComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val loader: suspend (Int) -> Result<PagedResponse<Post>>,
    @Assisted private val onNavigateToProfile: (String) -> Unit,
    @Assisted private val onNavigateToComments: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    @Assisted private val onNavigateToRepost: (Post) -> Unit,
    private val postRepository: PostRepository,
    private val shareService: ShareService,
) : PostListComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val feedDelegate: PostFeedDelegate by lazy {
        PostFeedDelegate(
            scope = scope,
            postRepository = postRepository,
            shareService = shareService,
            loader = { page -> loader(page) },
            onNewPost = { post ->
                if (post.parentPost == null) {
                    feedDelegate.prependItem(post)
                }
            }
        )
    }

    override val model = feedDelegate.state.map { PostListComponent.Model(it) }

    override fun insertNewPost(post: Post) = feedDelegate.prependItem(post)
    override fun onRefresh() = feedDelegate.onRefresh()
    override fun onLoadMore() = feedDelegate.onLoadMore()
    override fun onLikeClick(postId: Long) = feedDelegate.onLikeClick(postId)
    override fun onShareClick(post: Post) = feedDelegate.onShareClick(post)

    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onNavigateToDetails(post: Post) = onNavigateToComments(post)
    override fun onProfileClick(handle: String) = onNavigateToProfile(handle)
    override fun onHashtagClick(tag: String) = onNavigateToHashtag(tag)
    override fun onRepostClick(post: Post) = onNavigateToRepost(post)
}