package org.example.whiskr.component.details

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.example.whiskr.PagingDelegate
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia
import org.example.whiskr.util.mockPost

class FakePostDetailsComponent(
    initialModel: PostDetailsComponent.Model = PostDetailsComponent.Model(
        post = mockPost,
        isLoadingPost = false,
        isError = false,
        listState = PagingDelegate.State(
            items = emptyList(),
            isLoadingMore = false,
            isEndOfList = true
        )
    )
) : PostDetailsComponent {
    override val model: Value<PostDetailsComponent.Model> = MutableValue(initialModel)

    override fun onBackClick() {}
    override fun onLoadMore() {}
    override fun onReplyClick(post: Post) {}
    override fun onCommentsClick(post: Post) {}
    override fun onLikeClick(postId: Long) {}
    override fun onMediaClick(media: List<PostMedia>, index: Int) {}
    override fun onShareClick(post: Post) {}
    override fun onHashtagClick(tag: String) {}
}