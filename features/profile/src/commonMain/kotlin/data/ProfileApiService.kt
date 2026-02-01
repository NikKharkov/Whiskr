package data

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import org.example.whiskr.dto.ProfileResponse

interface ProfileApiService {
    @GET("profile/{handle}")
    suspend fun getFullProfile(@Path("handle") handle: String): FullProfileResponseDto

    @GET("profile/me")
    suspend fun getMyProfile(): ProfileResponse

    @POST("profile/{handle}/follow")
    suspend fun toggleFollow(@Path("handle") handle: String): ProfileResponse
}