package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia

interface HashtagsComponent {
    val hashtag: String
    val postsComponent: PostListComponent

    fun onBackClick()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            hashtag: String,
            onBack: () -> Unit,
            onNavigateToComments: (Post) -> Unit,
            onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
            onNavigateToHashtag: (String) -> Unit,
            onNavigateToProfile: (String) -> Unit,
            onNavigateToRepost: (Post) -> Unit
        ): HashtagsComponent
    }
}