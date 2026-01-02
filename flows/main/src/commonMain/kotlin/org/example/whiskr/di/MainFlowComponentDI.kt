package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.DefaultMainFlowComponent
import org.example.whiskr.component.MainFlowComponent

interface MainFlowComponentDI {
    @Provides
    @Singleton
    fun provideMainFlowFactory(
        factory: (ComponentContext, () -> Unit) -> DefaultMainFlowComponent
    ): MainFlowComponent.Factory {
        return MainFlowComponent.Factory(factory)
    }
}