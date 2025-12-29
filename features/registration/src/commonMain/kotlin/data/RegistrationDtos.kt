package data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileRequest(
    val handle: String,
    val displayName: String
)

@Serializable
data class CreatePetRequest(
    val name: String,
    val type: PetType,
    val gender: PetGender,
    val birthDate: LocalDate
)

@Serializable
enum class PetType {
    CAT, DOG, PARROT, HAMSTER, RABBIT, FISH, TURTLE, SNAKE, SPIDER, HORSE
}

@Serializable
enum class PetGender {
    MALE, FEMALE
}