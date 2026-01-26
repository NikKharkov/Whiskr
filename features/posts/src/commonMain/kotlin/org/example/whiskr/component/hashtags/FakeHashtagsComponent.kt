package org.example.whiskr.component.hashtags

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.example.whiskr.PagingDelegate
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia

class FakeHashtagsComponent(
    override val hashtag: String = "Whiskr",
    initialModel: HashtagsComponent.Model = HashtagsComponent.Model(
        listState = PagingDelegate.State(items = emptyList(), isLoadingMore = false, isEndOfList = false)
    )
) : HashtagsComponent {
    override val model: Value<HashtagsComponent.Model> = MutableValue(initialModel)

    override fun onBackClick() {}
    override fun onLoadMore() {}
    override fun onLikeClick(postId: Long) {}
    override fun onCommentsClick(post: Post) {}
    override fun onShareClick(post: Post) {}
    override fun onMediaClick(media: List<PostMedia>, index: Int) {}
    override fun onHashtagClick(tag: String) {}
}