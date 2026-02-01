package component.edit_profile

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import data.EditProfileRequest
import domain.AddEditRepository
import domain.UserState
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope

@Inject
class DefaultEditProfileComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted initialProfile: UserState,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onProfileUpdated: () -> Unit,
    private val addEditRepository: AddEditRepository
) : EditProfileComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val _model = MutableValue(
        EditProfileComponent.Model(
            name = initialProfile.profile?.displayName ?: "",
            handle = initialProfile.profile?.handle ?: "",
            bio = initialProfile.profile?.bio ?: "",
            avatarBytes = null,
            currentAvatarUrl = initialProfile.profile?.avatarUrl
        )
    )

    override val model: Value<EditProfileComponent.Model> = _model

    override fun onNameChanged(name: String) {
        _model.update { it.copy(name = name) }
    }

    override fun onHandleChanged(handle: String) {
        _model.update { it.copy(handle = handle) }
    }

    override fun onBioChanged(bio: String) {
        _model.update { it.copy(bio = bio) }
    }

    override fun onAvatarSelected(bytes: ByteArray) {
        _model.update { it.copy(avatarBytes = bytes) }
    }

    override fun onBackClicked() = onBack()

    override fun onSaveClicked() {
        val state = _model.value
        if (state.isLoading) return

        _model.update { it.copy(isLoading = true) }

        scope.launch {
            val request = EditProfileRequest(
                displayName = state.name.ifBlank { null },
                bio = state.bio.ifBlank { null },
                handle = state.handle.ifBlank { null }
            )

            addEditRepository.updateProfile(request, state.avatarBytes)
                .onSuccess {
                    _model.update { it.copy(isLoading = false) }
                    onProfileUpdated()
                }
                .onFailure { error ->
                    _model.update { it.copy(isLoading = false) }
                    Logger.e(error) { "Failed to update profile" }
                }
        }
    }
}