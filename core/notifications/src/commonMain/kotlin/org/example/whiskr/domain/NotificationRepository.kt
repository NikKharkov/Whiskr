package org.example.whiskr.domain

interface NotificationRepository {
    suspend fun syncToken()
}