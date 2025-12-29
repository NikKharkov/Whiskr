package component.pet

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import component.user.UserRegistrationComponent
import data.PetGender
import data.PetType
import kotlinx.datetime.LocalDate

interface PetRegistrationComponent {
    val model: Value<Model>

    fun onSkipClicked()
    fun onSaveClicked()
    fun onNameChanged(name: String)
    fun onTypeChanged(type: PetType)
    fun onGenderChanged(gender: PetGender)
    fun onBirthDateChanged(date: LocalDate)
    fun onAvatarSelected(avatarBytes: ByteArray)

    data class Model(
        val name: String? = null,
        val type: PetType? = null,
        val gender: PetGender? = null,
        val avatarBytes: ByteArray? = null,
        val birthDate: LocalDate? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSaveEnabled: Boolean = false
    ) {
        val showSkipButton: Boolean
            get() = name.isNullOrBlank() && type == null && gender == null && birthDate == null
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onFinished: () -> Unit
        ): PetRegistrationComponent
    }
}