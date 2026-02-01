package data

import domain.ProfileRepository
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.dto.ProfileResponse

@Inject
class ProfileRepositoryImpl(
    private val profileApiService: ProfileApiService
) : ProfileRepository {

    override suspend fun getFullProfile(handle: String): Result<FullProfileResponseDto> = runCatching {
        profileApiService.getFullProfile(handle)
    }

    override suspend fun getMyProfile(): Result<ProfileResponse> = runCatching {
        profileApiService.getMyProfile()
    }

    override suspend fun toggleFollow(handle: String): Result<ProfileResponse> = runCatching {
        profileApiService.toggleFollow(handle)
    }
}