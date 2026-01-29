package domain

import data.FullProfileResponseDto
import data.Profile

interface ProfileRepository {
    suspend fun getFullProfile(handle: String): Result<FullProfileResponseDto>
    suspend fun getMyProfile(): Result<Profile>
    suspend fun toggleFollow(handle: String): Result<Profile>
}