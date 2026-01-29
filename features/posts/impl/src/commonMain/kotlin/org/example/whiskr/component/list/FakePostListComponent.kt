package org.example.whiskr.component.list

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.example.whiskr.PagingDelegate
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.util.mockPost

class FakePostListComponent(
    initialItems: List<Post> = List(5) { index ->
        mockPost.copy(
            id = index + 100L,
            content = "This is a fake reply #$index.",
            stats = mockPost.stats.copy(likesCount = index * 5, repliesCount = 0)
        )
    }
) : PostListComponent {

    override val model: Value<PostListComponent.Model> = MutableValue(
        PostListComponent.Model(
            listState = PagingDelegate.State(
                items = initialItems,
                isLoadingMore = false,
                isEndOfList = true,
                isRefreshing = false,
                isError = false
            )
        )
    )

    override fun onRefresh() {}
    override fun onLoadMore() {}
    override fun onLikeClick(postId: Long) {}
    override fun onShareClick(post: Post) {}
    override fun onMediaClick(media: List<PostMedia>, index: Int) {}
    override fun onCommentsClick(post: Post) {}
    override fun onProfileClick(userId: Long) {}
    override fun onHashtagClick(tag: String) {}
    override fun insertNewPost(post: Post) {}
}