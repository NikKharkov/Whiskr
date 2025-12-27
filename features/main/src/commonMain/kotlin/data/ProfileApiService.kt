package data

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers

interface ProfileApiService {
    @Headers("Content-Type: application/json")
    @GET("profile/me")
    suspend fun getProfile(): ProfileResponseDto
}