package org.example.whiskr.component.home

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import org.example.whiskr.PagingDelegate
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.dto.Post
import org.example.whiskr.dto.PostMedia
import org.example.whiskr.util.mockFeed

class FakeHomeComponent(
    initialModel: HomeComponent.Model = HomeComponent.Model(
        listState = PagingDelegate.State(items = mockFeed, isLoadingMore = false, isEndOfList = false)
    )
) : HomeComponent {
    override val createPostComponent: CreatePostComponent = object : CreatePostComponent {
        override val model = MutableValue(CreatePostComponent.Model())
        override fun onTextChanged(text: String) {}
        override fun onMediaSelected(files: List<KmpFile>) {}
        override fun onRemoveFile(file: KmpFile) {}
        override fun onSendClick(context: PlatformContext) {}
        override fun onBackClick() {}
        override fun onRemoveUrl(url: String) {}
    }

    override val model: Value<HomeComponent.Model> = MutableValue(initialModel)

    override fun onRefresh() {}
    override fun onLoadMore() {}
    override fun onLikeClick(postId: Long) {}
    override fun onNavigateToCreatePostScreen() {}
    override fun onProfileClick(userId: Long) {}
    override fun onMediaClick(media: List<PostMedia>, index: Int) {}
    override fun onCommentsClick(post: Post) {}
    override fun onShareClick(post: Post) {}
    override fun onHashtagClick(tag: String) {}
}