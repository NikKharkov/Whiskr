package component.edit_pet

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import org.example.whiskr.dto.PetGender
import org.example.whiskr.dto.PetResponse
import org.example.whiskr.dto.PetType
import data.UpdatePetRequest
import domain.AddEditRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope

@Inject
class DefaultEditPetComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val petId: Long,
    @Assisted initialPetData: PetResponse,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onPetUpdated: () -> Unit,
    private val repository: AddEditRepository
) : EditPetComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val _model = MutableValue(
        EditPetComponent.Model(
            name = initialPetData.name,
            type = initialPetData.type,
            gender = initialPetData.gender,
            birthDate = initialPetData.birthDate,
            avatarBytes = null,
            currentAvatarUrl = initialPetData.avatarUrl
        )
    )

    override val model: Value<EditPetComponent.Model> = _model

    override fun onBackClicked() = onBack()

    override fun onSaveClicked() {
        val state = _model.value
        if (state.isLoading) return

        _model.update { it.copy(isLoading = true) }

        scope.launch {
            repository.updatePet(
                petId,
                UpdatePetRequest(state.name, state.type, state.gender, state.birthDate),
                state.avatarBytes
            )
                .onSuccess {
                    _model.update { it.copy(isLoading = false) }
                    onPetUpdated()
                }
                .onFailure { error ->
                    _model.update { it.copy(isLoading = false) }
                    Logger.e(error) { "Failed to update pet" }
                }
        }
    }

    override fun onNameChanged(name: String) {
        _model.update { it.copy(name = name) }
    }

    override fun onTypeChanged(type: PetType) {
        _model.update { it.copy(type = type) }
    }

    override fun onGenderChanged(gender: PetGender) {
        _model.update { it.copy(gender = gender) }
    }

    override fun onBirthDateChanged(date: LocalDate) {
        _model.update { it.copy(birthDate = date) }
    }

    override fun onAvatarSelected(bytes: ByteArray) {
        _model.update { it.copy(avatarBytes = bytes) }
    }
}