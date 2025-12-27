package domain

import data.ProfileResponseDto

interface ProfileRepository {
    suspend fun getMyProfile(): Result<ProfileResponseDto>
}