package component.edit_profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import domain.UserState

interface EditProfileComponent {
    val model: Value<Model>

    fun onNameChanged(name: String)
    fun onHandleChanged(handle: String)
    fun onBioChanged(bio: String)
    fun onAvatarSelected(bytes: ByteArray)
    fun onSaveClicked()
    fun onBackClicked()

    data class Model(
        val name: String,
        val handle: String,
        val bio: String,
        val avatarBytes: ByteArray?,
        val currentAvatarUrl: String?,
        val isLoading: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            initialProfile: UserState,
            onBack: () -> Unit,
            onProfileUpdated: () -> Unit
        ): EditProfileComponent
    }
}