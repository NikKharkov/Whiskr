package org.example.whiskr.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreatePetRequest(
    val name: String,
    val type: PetType,
    val gender: PetGender,
    val birthDate: LocalDate
)

@Serializable
data class PetResponse(
    val id: Long,
    val name: String,
    val type: PetType,
    val gender: PetGender,
    val birthDate: LocalDate,
    val avatarUrl: String?,
    val ownerHandle: String
)

@Serializable
enum class PetType {
    CAT, DOG, PARROT, HAMSTER, RABBIT, FISH, TURTLE, SNAKE, SPIDER, HORSE
}

@Serializable
enum class PetGender {
    MALE, FEMALE
}