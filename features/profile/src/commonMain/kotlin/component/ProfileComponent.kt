package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.data.Post
import org.example.whiskr.dto.Media
import org.example.whiskr.dto.PetResponse
import org.example.whiskr.dto.ProfileResponse

interface ProfileComponent {
    val model: Value<Model>
    val postsComponent: PostListComponent

    fun onBackClick()
    fun onFollowClick()
    fun onEditProfileClick()
    fun onNavigateToUserProfile(handle: String)
    fun onNavigateToPost(post: Post)
    fun onMediaClick(media: List<Media>, index: Int)
    fun onHashtagClick(tag: String)
    fun onPetClick(petId: Long, pet: PetResponse)
    fun onAddPetClick()
    fun onMessageClick()

    data class Model(
        val profile: ProfileResponse? = null,
        val pets: List<PetResponse> = emptyList(),
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
            onNavigateToMediaViewer: (List<Media>, Int) -> Unit,
            onNavigateToHashtag: (String) -> Unit,
            onNavigateToRepost: (Post) -> Unit,
            onNavigateToEditProfile: () -> Unit,
            onNavigateToAddPet: () -> Unit,
            onNavigateToEditPet: (Long, PetResponse) -> Unit,
            onSendMessageClick: (Long) -> Unit
        ): ProfileComponent
    }
}