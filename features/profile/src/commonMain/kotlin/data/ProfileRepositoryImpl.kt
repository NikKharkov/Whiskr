package data

import domain.ProfileRepository
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileRepositoryImpl(
    private val profileApiService: ProfileApiService
) : ProfileRepository {

    override suspend fun getFullProfile(handle: String): Result<FullProfileResponseDto> = runCatching {
        profileApiService.getFullProfile(handle)
    }

    override suspend fun getMyProfile(): Result<Profile> = runCatching {
        profileApiService.getMyProfile()
    }

    override suspend fun toggleFollow(handle: String): Result<Profile> = runCatching {
        profileApiService.toggleFollow(handle)
    }
}