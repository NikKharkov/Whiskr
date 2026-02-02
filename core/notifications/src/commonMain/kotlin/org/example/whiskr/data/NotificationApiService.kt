package org.example.whiskr.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface NotificationApiService {
    @Headers("Content-Type: application/json")
    @POST("notifications/device")
    suspend fun registerDevice(@Body request: DeviceTokenRequest)
}