package org.example.whiskr.component.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.operator.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.PagingDelegate
import org.example.whiskr.component.componentScope
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.delegates.PostFeedDelegate
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.domain.ShareService
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

@Inject
class DefaultHomeComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNavigateToCreatePost: () -> Unit,
    @Assisted private val onNavigateToProfile: (Long) -> Unit,
    @Assisted private val onNavigateToComments: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    private val postRepository: PostRepository,
    private val shareService: ShareService,
    private val createPostFactory: CreatePostComponent.Factory
) : HomeComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val pagingDelegate = PagingDelegate(
        scope = scope,
        loader = { page -> postRepository.getFeed(page) }
    )

    private val feedDelegate: PostFeedDelegate by lazy {
        PostFeedDelegate(
            scope = scope,
            postRepository = postRepository,
            shareService = shareService,
            loader = { page -> postRepository.getFeed(page) },
            onNewPost = { post ->
                if (post.parentPost == null) {
                    feedDelegate.prependItem(post)
                }
            }
        )
    }

    override val model = feedDelegate.state.map { HomeComponent.Model(it) }

    override val createPostComponent: CreatePostComponent = createPostFactory(
        componentContext = childContext("EmbeddedCreatePost"),
        initialImageUrl = null,
        onPostCreated = { newPost ->
            if (newPost.parentPost == null) {
                pagingDelegate.prependItem(newPost)
            }
        },
        onBack = {}
    )

    override fun onRefresh() = feedDelegate.onRefresh()
    override fun onLoadMore() = feedDelegate.onLoadMore()
    override fun onLikeClick(postId: Long) = feedDelegate.onLikeClick(postId)
    override fun onShareClick(post: Post) = feedDelegate.onShareClick(post)

    override fun onNavigateToCreatePostScreen() = onNavigateToCreatePost()
    override fun onProfileClick(userId: Long) = onNavigateToProfile(userId)
    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onCommentsClick(post: Post) = onNavigateToComments(post)
    override fun onHashtagClick(tag: String) = onNavigateToHashtag(tag)
}