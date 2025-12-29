package org.example.whiskr.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.HttpResponse
import org.example.whiskr.dto.AuthResponseDto

interface AuthApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun socialLogin(
        @Body request: SocialLoginRequestDto,
    ): AuthResponseDto

    @Headers("Content-Type: application/json")
    @POST("auth/otp")
    suspend fun requestOtp(
        @Body request: OtpRequestDto,
    ): HttpResponse

    @Headers("Content-Type: application/json")
    @POST("auth/verify")
    suspend fun verifyOtp(
        @Body request: VerifyRequestDto,
    ): AuthResponseDto
}
