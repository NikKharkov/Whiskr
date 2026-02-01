package data

import kotlinx.serialization.Serializable
import org.example.whiskr.data.Post
import org.example.whiskr.dto.PagedResponse
import org.example.whiskr.dto.PetResponse
import org.example.whiskr.dto.ProfileResponse

@Serializable
data class FullProfileResponseDto(
    val profile: ProfileResponse,
    val pets: List<PetResponse>,
    val posts: PagedResponse<Post>
)