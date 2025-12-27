package component.user

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface UserRegistrationComponent {

    val model: Value<Model>

    fun onNextClicked()
    fun onNameChanged(name: String)
    fun onUsernameChanged(handle: String)
    fun onAvatarSelected(avatarBytes: ByteArray)

    data class Model(
        val name: String = "",
        val username: String = "",
        val avatarBytes: ByteArray? = null,
        val isLoading: Boolean = false,
        val usernameError: String? = null,
        val isSubmitEnabled: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNextClicked: () -> Unit
        ): UserRegistrationComponent
    }
}