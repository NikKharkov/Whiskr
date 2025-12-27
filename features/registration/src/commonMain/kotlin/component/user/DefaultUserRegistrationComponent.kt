package component.user

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import domain.RegistrationRepository
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope

@Inject
class DefaultUserRegistrationComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onRegistrationFinished: () -> Unit,
    private val repository: RegistrationRepository
) : UserRegistrationComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val _model = MutableValue(UserRegistrationComponent.Model())
    override val model: Value<UserRegistrationComponent.Model> = _model

    override fun onNameChanged(name: String) {
        _model.update {
            it.copy(
                name = name,
                isSubmitEnabled = validate(name, it.username)
            )
        }
    }

    override fun onUsernameChanged(handle: String) {
        _model.update {
            it.copy(
                username = handle,
                usernameError = null,
                isSubmitEnabled = validate(it.name, handle)
            )
        }
    }

    override fun onAvatarSelected(avatarBytes: ByteArray) {
        _model.update { it.copy(avatarBytes = avatarBytes) }
    }

    override fun onNextClicked() {
        val state = _model.value
        if (!state.isSubmitEnabled) return

        _model.update { it.copy(isLoading = true) }

        scope.launch {
            repository.registerProfile(
                name = state.name,
                username = "@${state.username}",
                avatar = state.avatarBytes
            )
                .onSuccess {
                    _model.update { it.copy(isLoading = false) }
                    onRegistrationFinished()
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: "Unknown error"

                    if (errorMessage.contains("taken", ignoreCase = true)) {
                        _model.update {
                            it.copy(
                                isLoading = false,
                                usernameError = "Handle already taken"
                            )
                        }
                    } else {
                        _model.update {
                            it.copy(
                                isLoading = false,
                                usernameError = errorMessage
                            )
                        }
                    }

                    Logger.e(error) { "Failed to register profile: $errorMessage" }
                }
        }
    }

    private fun validate(name: String, username: String): Boolean {
        return name.isNotBlank() && username.isNotBlank()
    }
}