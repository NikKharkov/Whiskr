package component.add_pet

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.dto.PetGender
import org.example.whiskr.dto.PetType
import kotlinx.datetime.LocalDate

interface AddPetComponent {
    val model: Value<Model>

    fun onNameChanged(name: String)
    fun onTypeChanged(type: PetType)
    fun onGenderChanged(gender: PetGender)
    fun onBirthDateChanged(date: LocalDate)
    fun onAvatarSelected(bytes: ByteArray)
    fun onSaveClicked()
    fun onBackClicked()

    data class Model(
        val name: String = "",
        val type: PetType? = null,
        val gender: PetGender? = null,
        val birthDate: LocalDate? = null,
        val avatarBytes: ByteArray? = null,
        val isLoading: Boolean = false,
        val isSaveEnabled: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBack: () -> Unit,
            onPetAdded: () -> Unit
        ): AddPetComponent
    }
}