package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.DefaultNotificationComponent
import org.example.whiskr.component.NotificationComponent
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

    @Provides
    @Singleton
    fun provideNotificationComponentFactory(
        factory: (
            ComponentContext,
            () -> Unit,           // onBack
            (String) -> Unit      // onNavigateToDeepLink
        ) -> DefaultNotificationComponent
    ): NotificationComponent.Factory {
        return NotificationComponent.Factory(factory)
    }
}