package data

import co.touchlab.kermit.Logger
import domain.ProfileRepository
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileRepositoryImpl(
    private val profileApiService: ProfileApiService
) : ProfileRepository {
    override suspend fun getMyProfile(): Result<ProfileResponseDto> {
        return try {
            val response = profileApiService.getProfile()
            Result.success(response)
        } catch (e: Exception) {
            Logger.withTag("ProfileRepository").e(e) { "Failed to fetch profile" }
            Result.failure(e)
        }
    }
}