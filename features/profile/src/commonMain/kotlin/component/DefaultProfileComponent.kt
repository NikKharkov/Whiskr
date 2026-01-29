package component

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import domain.ProfileRepository
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.component.componentScope
import org.example.whiskr.data.Post
import org.example.whiskr.data.PostMedia
import org.example.whiskr.domain.PostRepository

@Inject
class DefaultProfileComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val handle: String,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onNavigateToPost: (Post) -> Unit,
    @Assisted private val onNavigateToUserProfile: (String) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<PostMedia>, Int) -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    private val profileRepository: ProfileRepository,
    private val postRepository: PostRepository,
    postListFactory: PostListComponent.Factory
) : ProfileComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableValue(ProfileComponent.Model())
    override val model: Value<ProfileComponent.Model> = _model

    override val postsComponent: PostListComponent = postListFactory(
        componentContext = childContext("ProfilePosts"),
        loader = { page -> postRepository.getPostsByHandle(handle, page) },
        onNavigateToProfile = { clickedHandle ->
            if (clickedHandle != handle) {
                onNavigateToUserProfile(clickedHandle)
            }
        },
        onNavigateToComments = { post -> onNavigateToPost(post) },
        onNavigateToMediaViewer = onNavigateToMediaViewer,
        onNavigateToHashtag = onNavigateToHashtag
    )

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        scope.launch {
            _model.value = _model.value.copy(isLoading = true)

            profileRepository.getFullProfile(handle)
                .onSuccess { fullProfile ->
                    _model.value = _model.value.copy(
                        isLoading = false,
                        profile = fullProfile.profile,
                        pets = fullProfile.pets
                    )
                }
                .onFailure { error ->
                    _model.value = _model.value.copy(isLoading = false, isError = true)
                    Logger.e(error) {"${error.message}"}
                }
        }
    }

    override fun onFollowClick() {
        val currentProfile = _model.value.profile ?: return

        val newFollowState = !currentProfile.isFollowing
        val newCount = currentProfile.followersCount + (if (newFollowState) 1 else -1)

        val updatedProfile = currentProfile.copy(
            isFollowing = newFollowState,
            followersCount = newCount
        )

        _model.value = _model.value.copy(profile = updatedProfile)

        scope.launch {
            profileRepository.toggleFollow(handle)
                .onSuccess { serverProfile ->
                    _model.value = _model.value.copy(profile = serverProfile)
                }
                .onFailure { error ->
                    _model.value = _model.value.copy(profile = currentProfile)
                    Logger.e(error) {"${error.message}"}
                }
        }
    }

    override fun onBackClick() = onBack()
    override fun onNavigateToUserProfile(handle: String) = onNavigateToUserProfile.invoke(handle)
    override fun onNavigateToPost(post: Post) = onNavigateToPost.invoke(post)
    override fun onMediaClick(media: List<PostMedia>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onHashtagClick(tag: String) = onNavigateToHashtag(tag)
    override fun onPetClick(petId: Long) { /* TODO */ }
    override fun onEditProfileClick() { /* TODO */ }
}