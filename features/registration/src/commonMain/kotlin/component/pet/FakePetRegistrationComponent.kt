package component.pet

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.datetime.LocalDate
import org.example.whiskr.dto.PetGender
import org.example.whiskr.dto.PetType

class FakePetRegistrationComponent(
    initialModel: PetRegistrationComponent.Model = PetRegistrationComponent.Model(
        name = "Barsik",
        type = PetType.CAT,
        gender = PetGender.MALE,
        birthDate = LocalDate(2023, 5, 20),
        isSaveEnabled = true
    )
) : PetRegistrationComponent {

    override val model: Value<PetRegistrationComponent.Model> = MutableValue(initialModel)

    override fun onNameChanged(name: String) {}
    override fun onTypeChanged(type: PetType) {}
    override fun onGenderChanged(gender: PetGender) {}
    override fun onBirthDateChanged(date: LocalDate) {}
    override fun onAvatarSelected(avatarBytes: ByteArray) {}

    override fun onSkipClicked() {}
    override fun onSaveClicked() {}
    override fun onBackClicked() {}
}