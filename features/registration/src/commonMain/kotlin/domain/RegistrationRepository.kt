package domain

interface RegistrationRepository {
    suspend fun registerProfile(name: String, username: String, avatar: ByteArray?): Result<Unit>
}