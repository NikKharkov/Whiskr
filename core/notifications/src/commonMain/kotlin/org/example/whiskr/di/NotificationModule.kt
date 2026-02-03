package org.example.whiskr.di

import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.data.NotificationApiService
import org.example.whiskr.data.NotificationRepositoryImpl
import org.example.whiskr.data.createNotificationApiService
import org.example.whiskr.domain.NotificationRepository

interface NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationApiService(ktorfit: Ktorfit): NotificationApiService = ktorfit.createNotificationApiService()

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationApiService: NotificationApiService): NotificationRepository = NotificationRepositoryImpl(notificationApiService)
}