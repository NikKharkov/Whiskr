package component.user

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class FakeUserRegistrationComponent(
    initialModel: UserRegistrationComponent.Model = UserRegistrationComponent.Model(
        name = "user",
        username = "user",
        isSubmitEnabled = true
    )
) : UserRegistrationComponent {

    override val model: Value<UserRegistrationComponent.Model> = MutableValue(initialModel)

    override fun onNextClicked() {}
    override fun onNameChanged(name: String) {}
    override fun onUsernameChanged(handle: String) {}
    override fun onAvatarSelected(avatarBytes: ByteArray) {}
}