package data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.statement.HttpResponse

interface RegistrationApiService {

    @POST("profile/register")
    suspend fun registerUser(@Body body: MultiPartFormDataContent): HttpResponse
}