package data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.example.whiskr.dto.PetGender
import org.example.whiskr.dto.PetType

@Serializable
data class EditProfileRequest(
    val displayName: String? = null,
    val bio: String? = null,
    val handle: String? = null
)

@Serializable
data class UpdatePetRequest(
    val name: String,
    val type: PetType,
    val gender: PetGender,
    val birthDate: LocalDate
)

