package di

import com.arkivanov.decompose.ComponentContext
import component.DefaultMainComponent
import component.MainComponent
import data.ProfileApiService
import data.ProfileRepositoryImpl
import data.createProfileApiService
import de.jensklingenberg.ktorfit.Ktorfit
import domain.ProfileRepository
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.di.Singleton

interface MainComponentDI {

    @Provides
    @Singleton
    fun provideMainRepository(profileApi: ProfileApiService): ProfileRepository =
        ProfileRepositoryImpl(profileApi)

    @Provides
    @Singleton
    fun provideMainApiService(ktorfit: Ktorfit): ProfileApiService = ktorfit.createProfileApiService()

    @Provides
    @Singleton
    fun provideMainScreenFactory(
        factory: (ComponentContext, () -> Unit, () -> Unit) -> DefaultMainComponent
    ): MainComponent.Factory {
        return MainComponent.Factory(factory)
    }
}