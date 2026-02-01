package domain

import data.FullProfileResponseDto
import org.example.whiskr.dto.ProfileResponse

interface ProfileRepository {
    suspend fun getFullProfile(handle: String): Result<FullProfileResponseDto>
    suspend fun getMyProfile(): Result<ProfileResponse>
    suspend fun toggleFollow(handle: String): Result<ProfileResponse>
}