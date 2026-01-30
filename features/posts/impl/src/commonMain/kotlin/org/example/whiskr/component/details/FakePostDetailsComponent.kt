package org.example.whiskr.component.details

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.example.whiskr.component.PostDetailsComponent
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.component.list.FakePostListComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.util.mockPost

class FakePostDetailsComponent(
    initialModel: PostDetailsComponent.Model = PostDetailsComponent.Model(
        post = mockPost,
        isLoadingPost = false,
        isError = false,
    )
) : PostDetailsComponent {
    override val model: Value<PostDetailsComponent.Model> = MutableValue(initialModel)
    override val repliesComponent: PostListComponent = FakePostListComponent()

    override fun onBackClick() {}
    override fun onReplyClick(post: Post) {}
    override fun onLikeClick(postId: Long) {}
    override fun onMediaClick(media: List<PostMedia>, index: Int) {}
    override fun onShareClick(post: Post) {}
    override fun onNavigateToParentProfile(handle: String) {}
    override fun onRepostClick(post: Post) {}
}