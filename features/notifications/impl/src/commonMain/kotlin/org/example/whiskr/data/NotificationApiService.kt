package org.example.whiskr.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import org.example.whiskr.dto.PagedResponse

interface NotificationApiService {
    @Headers("Content-Type: application/json")
    @POST("notifications/device")
    suspend fun registerDevice(@Body request: DeviceTokenRequest)

    @Headers("Content-Type: application/json")
    @GET("notifications")
    suspend fun getNotifications(
        @Query("page") page: Int,
        @Query("size") size: Int = 20
    ): PagedResponse<Notification>

    @Headers("Content-Type: application/json")
    @GET("notifications/unread-count")
    suspend fun getUnreadCount(): Long

    @Headers("Content-Type: application/json")
    @POST("notifications/mark-read")
    suspend fun markAllRead()
}