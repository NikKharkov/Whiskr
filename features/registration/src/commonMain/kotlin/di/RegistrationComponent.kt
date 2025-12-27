package di

import com.arkivanov.decompose.ComponentContext
import component.user.DefaultUserRegistrationComponent
import component.user.UserRegistrationComponent
import data.RegistrationApiService
import data.RegistrationRepositoryImpl
import data.createRegistrationApiService
import de.jensklingenberg.ktorfit.Ktorfit
import domain.RegistrationRepository
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.di.Singleton

interface RegistrationComponent {

    @Provides
    @Singleton
    fun provideRegistrationApiService(ktorfit: Ktorfit): RegistrationApiService = ktorfit.createRegistrationApiService()

    @Provides
    @Singleton
    fun provideRegistrationRepository(
        registrationApiService: RegistrationApiService
    ): RegistrationRepository = RegistrationRepositoryImpl(registrationApiService)

    @Provides
    @Singleton
    fun provideUserRegistrationFactory(
        factory: (ComponentContext, () -> Unit) -> DefaultUserRegistrationComponent
    ): UserRegistrationComponent.Factory {
        return UserRegistrationComponent.Factory(factory)
    }
}