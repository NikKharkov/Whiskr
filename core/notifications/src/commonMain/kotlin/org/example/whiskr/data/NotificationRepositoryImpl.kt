package org.example.whiskr.data

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.domain.NotificationRepository

@Inject
class NotificationRepositoryImpl(
    private val notificationApiService: NotificationApiService
): NotificationRepository {
    override suspend fun syncToken() {
        val token = NotifierManager.getPushNotifier().getToken() ?: return

        try {
            notificationApiService.registerDevice(DeviceTokenRequest(token = token))
        } catch (e: Exception) {
            Logger.e(e) { "Error sending token" }
        }
    }
}