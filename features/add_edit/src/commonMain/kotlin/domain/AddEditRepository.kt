package domain

import org.example.whiskr.dto.CreatePetRequest
import data.EditProfileRequest
import org.example.whiskr.dto.PetResponse
import org.example.whiskr.dto.ProfileResponse
import data.UpdatePetRequest

interface AddEditRepository {
    suspend fun updateProfile(request: EditProfileRequest, avatarBytes: ByteArray?): Result<ProfileResponse>
    suspend fun addPet(request: CreatePetRequest, avatarBytes: ByteArray?): Result<PetResponse>
    suspend fun updatePet(petId: Long, request: UpdatePetRequest, avatarBytes: ByteArray?): Result<PetResponse>
}