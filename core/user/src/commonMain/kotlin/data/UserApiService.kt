package data

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers

interface UserApiService {
    @Headers("Content-Type: application/json")
    @GET("profile/me")
    suspend fun getProfile(): UserResponseDto
}