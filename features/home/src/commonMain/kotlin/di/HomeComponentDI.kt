package di

import com.arkivanov.decompose.ComponentContext
import component.DefaultHomeComponent
import component.HomeComponent
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.di.Singleton

interface HomeComponentDI {

    @Provides
    @Singleton
    fun provideHomeFactory(
        factory: (ComponentContext, () -> Unit, suspend () -> Unit) -> DefaultHomeComponent
    ): HomeComponent.Factory {
        return HomeComponent.Factory(factory)
    }
}