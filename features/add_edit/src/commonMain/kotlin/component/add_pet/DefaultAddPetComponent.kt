package component.add_pet

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import org.example.whiskr.dto.CreatePetRequest
import org.example.whiskr.dto.PetGender
import org.example.whiskr.dto.PetType
import domain.AddEditRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope

@Inject
class DefaultAddPetComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onPetAdded: () -> Unit,
    private val repository: AddEditRepository
) : AddPetComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val _model = MutableValue(AddPetComponent.Model())

    override val model: Value<AddPetComponent.Model> = _model

    override fun onBackClicked() = onBack()

    override fun onSaveClicked() {
        val state = _model.value
        if (!state.isSaveEnabled || state.isLoading) return

        _model.update { it.copy(isLoading = true) }

        val name = state.name
        val type = state.type ?: return
        val gender = state.gender ?: return
        val birthDate = state.birthDate ?: return

        scope.launch {
            repository.addPet(
                CreatePetRequest(name, type, gender, birthDate),
                state.avatarBytes
            )
                .onSuccess {
                    _model.update { it.copy(isLoading = false) }
                    onPetAdded()
                }
                .onFailure { error ->
                    _model.update { it.copy(isLoading = false) }
                    Logger.e(error) { "Failed to add pet" }
                }
        }
    }

    override fun onNameChanged(name: String) {
        updateState { it.copy(name = name) }
    }

    override fun onTypeChanged(type: PetType) {
        updateState { it.copy(type = type) }
    }

    override fun onGenderChanged(gender: PetGender) {
        updateState { it.copy(gender = gender) }
    }

    override fun onBirthDateChanged(date: LocalDate) {
        updateState { it.copy(birthDate = date) }
    }

    override fun onAvatarSelected(bytes: ByteArray) {
        updateState { it.copy(avatarBytes = bytes) }
    }

    private fun updateState(reducer: (AddPetComponent.Model) -> AddPetComponent.Model) {
        _model.update { oldState ->
            val newState = reducer(oldState)
            val isValid = newState.name.isNotBlank() &&
                    newState.type != null &&
                    newState.gender != null &&
                    newState.birthDate != null

            newState.copy(isSaveEnabled = isValid)
        }
    }
}