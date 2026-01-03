package org.example.whiskr.root

import com.arkivanov.decompose.ComponentContext
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.DatabaseFactory
import org.example.whiskr.di.Singleton

interface RootComponentDI {
    @Provides
    @Singleton
    fun provideRootFactory(
        factory: (ComponentContext) -> DefaultRootComponent,
        database: DatabaseFactory
    ): RootComponent.Factory {
        return RootComponent.Factory(factory)
    }
}
