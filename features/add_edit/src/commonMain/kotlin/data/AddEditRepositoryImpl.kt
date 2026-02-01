package data

import domain.AddEditRepository
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.dto.CreatePetRequest
import org.example.whiskr.dto.PetResponse
import org.example.whiskr.dto.ProfileResponse

@Inject
class AddEditRepositoryImpl(
    private val addEditApiService: AddEditApiService,
) : AddEditRepository {

    override suspend fun updateProfile(
        request: EditProfileRequest,
        avatarBytes: ByteArray?
    ): Result<ProfileResponse> = runCatching {
        val body = createMultipartBody(
            jsonData = Json.encodeToString(request),
            avatarBytes = avatarBytes
        )
        addEditApiService.updateProfile(body)
    }

    override suspend fun addPet(
        request: CreatePetRequest,
        avatarBytes: ByteArray?
    ): Result<PetResponse> = runCatching {
        val body = createMultipartBody(
            jsonData = Json.encodeToString(request),
            avatarBytes = avatarBytes
        )
        addEditApiService.addPet(body)
    }

    override suspend fun updatePet(
        petId: Long,
        request: UpdatePetRequest,
        avatarBytes: ByteArray?
    ): Result<PetResponse> = runCatching {
        val body = createMultipartBody(
            jsonData = Json.encodeToString(request),
            avatarBytes = avatarBytes
        )
        addEditApiService.updatePet(petId, body)
    }

    private fun createMultipartBody(
        jsonData: String,
        avatarBytes: ByteArray?
    ): MultiPartFormDataContent {
        return MultiPartFormDataContent(
            formData {
                append(
                    key = "data",
                    value = jsonData,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "application/json")
                    }
                )

                if (avatarBytes != null) {
                    append(
                        key = "avatar",
                        value = avatarBytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"avatar.jpg\"")
                        }
                    )
                }
            }
        )
    }
}