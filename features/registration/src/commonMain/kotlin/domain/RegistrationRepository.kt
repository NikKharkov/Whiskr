package domain

import data.PetGender
import data.PetType
import kotlinx.datetime.LocalDate

interface RegistrationRepository {
    suspend fun registerProfile(name: String, username: String, avatar: ByteArray?): Result<Unit>
    suspend fun savePet(
        name: String,
        type: PetType,
        gender: PetGender,
        birthDate: LocalDate,
        avatar: ByteArray?
    ): Result<Unit>
}