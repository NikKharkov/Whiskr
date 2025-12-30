package component.pet

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import data.PetGender
import data.PetType
import domain.RegistrationRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope

@Inject
class DefaultPetRegistrationComponent(
    @Assisted private val componentContext: ComponentContext,
    @Assisted private val onFinished: () -> Unit,
    @Assisted private val onBack: () -> Unit,
    private val registrationRepository: RegistrationRepository
) : PetRegistrationComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val _model = MutableValue(PetRegistrationComponent.Model())

    override val model: Value<PetRegistrationComponent.Model> = _model

    override fun onSkipClicked() = onFinished()

    override fun onBackClicked() = onBack()

    override fun onSaveClicked() {
        val state = _model.value
        if (!state.isSaveEnabled) return

        _model.update { it.copy(isLoading = true, error = null) }

        val name = state.name ?: return
        val type = state.type ?: return
        val gender = state.gender ?: return
        val birthDate = state.birthDate ?: return

        scope.launch {
            registrationRepository.savePet(
                name = name,
                type = type,
                gender = gender,
                birthDate = birthDate,
                avatar = state.avatarBytes
            )
                .onSuccess {
                    _model.update { it.copy(isLoading = false) }
                    onFinished()
                }
                .onFailure { error ->
                    _model.update { it.copy(isLoading = false, error = error.message) }
                    Logger.e(error) { "Failed to register pet: ${error.message}" }
                }
        }
    }

    override fun onNameChanged(name: String) {
        updateState { it.copy(name = name, error = null) }
    }

    override fun onTypeChanged(type: PetType) {
        updateState { it.copy(type = type, error = null) }
    }

    override fun onGenderChanged(gender: PetGender) {
        updateState { it.copy(gender = gender, error = null) }
    }

    override fun onBirthDateChanged(date: LocalDate) {
        updateState { it.copy(birthDate = date, error = null) }
    }

    override fun onAvatarSelected(avatarBytes: ByteArray) {
        updateState { it.copy(avatarBytes = avatarBytes) }
    }

    private fun updateState(reducer: (PetRegistrationComponent.Model) -> PetRegistrationComponent.Model) {
        _model.update { oldState ->
            val newState = reducer(oldState)

            val isFormEmpty = newState.name.isNullOrBlank() &&
                    newState.type == null &&
                    newState.gender == null &&
                    newState.birthDate == null &&
                    newState.avatarBytes == null

            val isValid = !newState.name.isNullOrBlank() &&
                    newState.type != null &&
                    newState.gender != null &&
                    newState.birthDate != null

            newState.copy(
                isSkipMode = isFormEmpty,
                isSaveEnabled = isValid
            )
        }
    }
}