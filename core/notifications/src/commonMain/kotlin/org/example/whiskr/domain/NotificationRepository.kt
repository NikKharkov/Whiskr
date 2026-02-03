package org.example.whiskr.domain

interface NotificationRepository {
    suspend fun syncToken()
    suspend fun subscribeToUser(userId: Long)
    suspend fun unsubscribeFromUser(userId: Long)
}