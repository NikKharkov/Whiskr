package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import data.Pet
import data.Profile
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia

interface ProfileComponent {
    val model: Value<Model>
    val postsComponent: PostListComponent

    fun onBackClick()
    fun onFollowClick()
    fun onEditProfileClick()
    fun onNavigateToUserProfile(handle: String)
    fun onNavigateToPost(post: Post)
    fun onMediaClick(media: List<PostMedia>, index: Int)
    fun onHashtagClick(tag: String)
    fun onPetClick(petId: Long)

    data class Model(
        val profile: Profile? = null,
        val pets: List<Pet> = emptyList(),
        val isLoading: Boolean = true,
        val isError: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            handle: String,
            onBack: () -> Unit,
            onNavigateToPost: (Post) -> Unit,
            onNavigateToUserProfile: (String) -> Unit,
            onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
            onNavigateToHashtag: (String) -> Unit
        ): ProfileComponent
    }
}