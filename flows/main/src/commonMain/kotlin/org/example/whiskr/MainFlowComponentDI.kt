package org.example.whiskr

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.di.Singleton

interface MainFlowComponentDI {
    @Provides
    @Singleton
    fun provideMainFlowFactory(
        factory: (ComponentContext, () -> Unit) -> DefaultMainFlowComponent
    ): MainFlowComponent.Factory {
        return MainFlowComponent.Factory(factory)
    }
}