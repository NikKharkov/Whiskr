package data

import co.touchlab.kermit.Logger
import domain.RegistrationRepository
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class RegistrationRepositoryImpl(
    private val registrationApiService: RegistrationApiService
) : RegistrationRepository {

    override suspend fun registerProfile(
        name: String,
        username: String,
        avatar: ByteArray?
    ): Result<Unit> {
        return try {
            val requestDto = SetupProfileRequest(username, name)

            val multipartBody = MultiPartFormDataContent(
                formData {
                    append(
                        key = "data",
                        value = Json.encodeToString(requestDto),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        }
                    )
                    if (avatar != null) {
                        append(
                            key = "avatar",
                            value = avatar,
                            headers = Headers.build {
                                append(HttpHeaders.ContentDisposition, "filename=\"avatar.jpg\"")
                                append(HttpHeaders.ContentType, "image/jpeg")
                            }
                        )
                    }
                }
            )

            registrationApiService.registerUser(multipartBody)
            Result.success(Unit)
        } catch (e: Exception) {
            Logger.e(e) { "Registration error" }
            Result.failure(e)
        }
    }
}