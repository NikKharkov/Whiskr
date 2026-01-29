package org.example.whiskr.component.hashtags

import org.example.whiskr.component.HashtagsComponent
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.component.list.FakePostListComponent

class FakeHashtagsComponent(
    override val hashtag: String = "Whiskr",
    override val postsComponent: PostListComponent = FakePostListComponent()
) : HashtagsComponent {

    override fun onBackClick() {}
}