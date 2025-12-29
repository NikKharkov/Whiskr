package org.example.whiskr.data

import co.touchlab.kermit.Logger
import io.ktor.http.HttpStatusCode
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.TokenStorage
import org.example.whiskr.domain.AuthRepository

@Inject
class AuthRepositoryImpl(
    private val authApi: AuthApiService,
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override suspend fun login(idToken: String): Result<Unit> {
        return try {
            val response = authApi.socialLogin(SocialLoginRequestDto(idToken))

            tokenStorage.accessToken = response.accessToken
            tokenStorage.refreshToken = response.refreshToken

            Result.success(Unit)
        } catch (e: Exception) {
            Logger.withTag("AuthRepository").e(e) { "Google Login Failed" }
            Result.failure(e)
        }
    }

    override suspend fun requestOtp(email: String): Result<Unit> {
        return try {
            val response = authApi.requestOtp(OtpRequestDto(email))
            if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Server error: ${response.status}"))
            }
        } catch (e: Exception) {
            Logger.withTag("AuthRepository").e(e) { "OTP Request failed" }
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(
        email: String,
        code: String,
    ): Result<Unit> {
        return try {
            val response = authApi.verifyOtp(VerifyRequestDto(email, code))

            tokenStorage.accessToken = response.accessToken
            tokenStorage.refreshToken = response.refreshToken

            Result.success(Unit)
        } catch (e: Exception) {
            Logger.withTag("AuthRepository").e(e) { "OTP verification failed" }
            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return tokenStorage.accessToken != null && tokenStorage.refreshToken != null
    }
}
