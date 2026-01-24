package org.example.whiskr

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.di.Singleton

interface AuthFlowModule {
    @Provides
    @Singleton
    fun provideAuthFlowFactory(
        factory: (ComponentContext, () -> Unit) -> DefaultAuthFlowComponent
    ): AuthFlowComponent.Factory {
        return AuthFlowComponent.Factory(factory)
    }
}