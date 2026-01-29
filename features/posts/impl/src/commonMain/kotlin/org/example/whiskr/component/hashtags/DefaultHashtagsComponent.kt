package org.example.whiskr.component.hashtags

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.HashtagsComponent
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.domain.PostRepository

@Inject
class DefaultHashtagsComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted override val hashtag: String,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onNavigateToComments: (Post) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    @Assisted private val onNavigateToProfile: (String) -> Unit, // 👇
    private val postRepository: PostRepository,
    postListFactory: PostListComponent.Factory,
) : HashtagsComponent, ComponentContext by componentContext {

    override val postsComponent: PostListComponent = postListFactory(
        componentContext = childContext("HashtagFeed"),
        loader = { page -> postRepository.getPostsByHashtag(hashtag, page) },
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToComments = onNavigateToComments,
        onNavigateToMediaViewer = onNavigateToMediaViewer,
        onNavigateToHashtag = { tag ->
            if (tag != hashtag) onNavigateToHashtag(tag)
        }
    )

    override fun onBackClick() = onBack()
}