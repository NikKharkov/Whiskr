package data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.request.forms.MultiPartFormDataContent
import org.example.whiskr.dto.PetResponse
import org.example.whiskr.dto.ProfileResponse

interface AddEditApiService {

    @PATCH("profile/update")
    suspend fun updateProfile(@Body body: MultiPartFormDataContent): ProfileResponse

    @POST("pet/save")
    suspend fun addPet(@Body body: MultiPartFormDataContent): PetResponse

    @PATCH("pet/{id}/update")
    suspend fun updatePet(
        @Path("id") id: Long,
        @Body body: MultiPartFormDataContent
    ): PetResponse
}