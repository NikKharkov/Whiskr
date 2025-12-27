package org.example.whiskr.domain

interface AuthRepository {
    suspend fun login(idToken: String): Result<Unit>

    suspend fun requestOtp(email: String): Result<Unit>

    suspend fun verifyOtp(
        email: String,
        code: String,
    ): Result<Unit>

    fun isUserLoggedIn(): Boolean
}
