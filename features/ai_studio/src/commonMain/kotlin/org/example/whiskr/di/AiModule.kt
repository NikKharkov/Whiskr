package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.AiStudioComponent
import org.example.whiskr.component.DefaultAiStudioComponent
import org.example.whiskr.data.AiApiService
import org.example.whiskr.data.AiRepositoryImpl
import org.example.whiskr.data.createAiApiService
import org.example.whiskr.domain.AiRepository

interface AiModule {

    @Provides
    @Singleton
    fun provideAiApiService(ktorfit: Ktorfit): AiApiService = ktorfit.createAiApiService()

    @Provides
    @Singleton
    fun provideAiRepository(
        aiApiService: AiApiService
    ): AiRepository = AiRepositoryImpl(aiApiService)

    @Provides
    @Singleton
    fun provideAiStudioFactory(
        factory: (ComponentContext, Long, (String) -> Unit, (String) -> Unit) -> DefaultAiStudioComponent
    ): AiStudioComponent.Factory {
        return AiStudioComponent.Factory(factory)
    }
}