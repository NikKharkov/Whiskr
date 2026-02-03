package org.example.whiskr.data

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.domain.NotificationRepository
import org.example.whiskr.dto.PagedResponse

@Inject
class NotificationRepositoryImpl(
    private val notificationApiService: NotificationApiService
) : NotificationRepository {
    private val notifier = NotifierManager.getPushNotifier()

    override suspend fun syncToken() {
        val token = notifier.getToken() ?: return

        try {
            notificationApiService.registerDevice(DeviceTokenRequest(token = token))
        } catch (e: Exception) {
            Logger.e(e) { "Error sending token" }
        }
    }

    override suspend fun subscribeToUser(userId: Long) {
        val topic = "user_posts_$userId"
        notifier.subscribeToTopic(topic)
    }

    override suspend fun unsubscribeFromUser(userId: Long) {
        val topic = "user_posts_$userId"
        notifier.unSubscribeFromTopic(topic)
    }

    override suspend fun getNotifications(page: Int): Result<PagedResponse<Notification>> {
        return runCatching {
            notificationApiService.getNotifications(page = page)
        }
    }

    override suspend fun getUnreadCount(): Result<Long> {
        return runCatching {
            notificationApiService.getUnreadCount()
        }
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        return runCatching {
            notificationApiService.markAllRead()
        }
    }
}