package org.example.whiskr.domain

import org.example.whiskr.data.Notification
import org.example.whiskr.dto.PagedResponse

interface NotificationRepository {
    suspend fun syncToken()
    suspend fun subscribeToUser(userId: Long)
    suspend fun unsubscribeFromUser(userId: Long)
    suspend fun getNotifications(page: Int): Result<PagedResponse<Notification>>
    suspend fun getUnreadCount(): Result<Long>
    suspend fun markAllAsRead(): Result<Unit>
}