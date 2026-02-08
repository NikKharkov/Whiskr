package component

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import org.example.whiskr.dto.PetResponse
import domain.ProfileRepository
import domain.UserRepository
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.component.componentScope
import org.example.whiskr.data.Post
import org.example.whiskr.dto.Media
import org.example.whiskr.domain.NotificationRepository
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.dto.ProfileResponse
import kotlin.onSuccess

@Inject
class DefaultProfileComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val handle: String,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onNavigateToPost: (Post) -> Unit,
    @Assisted private val onNavigateToUserProfile: (String) -> Unit,
    @Assisted private val onNavigateToMediaViewer: (List<Media>, Int) -> Unit,
    @Assisted private val onNavigateToHashtag: (String) -> Unit,
    @Assisted private val onNavigateToRepost: (Post) -> Unit,
    @Assisted private val onNavigateToEditProfile: () -> Unit,
    @Assisted private val onNavigateToAddPet: () -> Unit,
    @Assisted private val onNavigateToEditPet: (Long, PetResponse) -> Unit,
    @Assisted private val onSendMessageClick: (Long) -> Unit,
    private val profileRepository: ProfileRepository,
    private val postRepository: PostRepository,
    private val notificationRepository: NotificationRepository,
    userRepository: UserRepository,
    postListFactory: PostListComponent.Factory
) : ProfileComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private var currentHandle = handle

    private val _model = MutableValue(ProfileComponent.Model())
    override val model: Value<ProfileComponent.Model> = _model

    override val postsComponent: PostListComponent = postListFactory(
        componentContext = childContext("ProfilePosts"),
        loader = { page -> postRepository.getPostsByHandle(currentHandle, page) },
        onNavigateToProfile = { clickedHandle ->
            if (clickedHandle != currentHandle) {
                onNavigateToUserProfile(clickedHandle)
            }
        },
        onNavigateToComments = { post -> onNavigateToPost(post) },
        onNavigateToMediaViewer = onNavigateToMediaViewer,
        onNavigateToHashtag = onNavigateToHashtag,
        onNavigateToRepost = onNavigateToRepost
    )

    init {
        loadProfileData()

        val cancellation = userRepository.user.subscribe { userState ->
            val user = userState.profile ?: return@subscribe

            val currentProfileId = _model.value.profile?.id
            val isMe = (currentProfileId != null && user.id == currentProfileId) || (user.handle == currentHandle)

            if (isMe) {
                if (currentHandle != user.handle) {
                    currentHandle = user.handle
                    postsComponent.onRefresh()
                }

                _model.update { currentModel ->
                    val updatedProfile = ProfileResponse(
                        id = user.id,
                        userId = user.userId,
                        handle = user.handle,
                        displayName = user.displayName,
                        bio = user.bio,
                        avatarUrl = user.avatarUrl,
                        followersCount = user.followersCount,
                        followingCount = currentModel.profile?.followingCount ?: 0,
                        isFollowing = false,
                        isMe = true
                    )
                    currentModel.copy(profile = updatedProfile)
                }

                loadProfileData(isSilent = true)
            }
        }
        lifecycle.doOnDestroy { cancellation.cancel() }
    }

    private fun loadProfileData(isSilent: Boolean = false) {
        scope.launch {
            if (!isSilent) {
                _model.update { it.copy(isLoading = true) }
            }

            profileRepository.getFullProfile(currentHandle)
                .onSuccess { fullProfile ->
                    _model.update {
                        it.copy(
                            profile = fullProfile.profile,
                            pets = fullProfile.pets,
                            isLoading = false,
                            isError = false
                        )
                    }
                }
                .onFailure {
                    if (!isSilent) {
                        _model.update { it.copy(isLoading = false, isError = true) }
                    }
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
            profileRepository.toggleFollow(currentHandle)
                .onSuccess { newProfile ->
                    _model.value = _model.value.copy(profile = newProfile)

                    if (newProfile.isFollowing) {
                        notificationRepository.subscribeToUser(newProfile.id)
                    } else {
                        notificationRepository.unsubscribeFromUser(newProfile.id)
                    }
                }
                .onFailure { error ->
                    _model.value = _model.value.copy(profile = currentProfile)
                    Logger.e(error) { "${error.message}" }
                }
        }
    }

    override fun onMessageClick() {
        val profile = _model.value.profile ?: return
        if (profile.isMe) return

        onSendMessageClick(profile.userId)
    }

    override fun onBackClick() = onBack()
    override fun onNavigateToUserProfile(handle: String) = onNavigateToUserProfile.invoke(handle)
    override fun onNavigateToPost(post: Post) = onNavigateToPost.invoke(post)
    override fun onMediaClick(media: List<Media>, index: Int) = onNavigateToMediaViewer(media, index)
    override fun onHashtagClick(tag: String) = onNavigateToHashtag(tag)
    override fun onPetClick(petId: Long, pet: PetResponse) = onNavigateToEditPet(petId, pet)
    override fun onEditProfileClick() = onNavigateToEditProfile()
    override fun onAddPetClick() = onNavigateToAddPet()
}