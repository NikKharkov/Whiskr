package component.edit_pet

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.dto.PetGender
import org.example.whiskr.dto.PetResponse
import org.example.whiskr.dto.PetType
import kotlinx.datetime.LocalDate

interface EditPetComponent {
    val model: Value<Model>

    fun onNameChanged(name: String)
    fun onTypeChanged(type: PetType)
    fun onGenderChanged(gender: PetGender)
    fun onBirthDateChanged(date: LocalDate)
    fun onAvatarSelected(bytes: ByteArray)
    fun onSaveClicked()
    fun onBackClicked()

    data class Model(
        val name: String,
        val type: PetType,
        val gender: PetGender,
        val birthDate: LocalDate,
        val avatarBytes: ByteArray?,
        val currentAvatarUrl: String?,
        val isLoading: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            petId: Long,
            initialPetData: PetResponse,
            onBack: () -> Unit,
            onPetUpdated: () -> Unit
        ): EditPetComponent
    }
}