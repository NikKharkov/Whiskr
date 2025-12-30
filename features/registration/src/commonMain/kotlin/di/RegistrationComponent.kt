package di

import com.arkivanov.decompose.ComponentContext
import component.DefaultRegistrationWizardComponent
import component.RegistrationWizardComponent
import component.pet.DefaultPetRegistrationComponent
import component.pet.PetRegistrationComponent
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

    @Provides
    @Singleton
    fun providePetRegistrationFactory(
        factory: (ComponentContext, () -> Unit, () -> Unit) -> DefaultPetRegistrationComponent
    ): PetRegistrationComponent.Factory {
        return PetRegistrationComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideRegistrationWizardFactory(
        factory: (ComponentContext, () -> Unit) -> DefaultRegistrationWizardComponent
    ): RegistrationWizardComponent.Factory {
        return RegistrationWizardComponent.Factory(factory)
    }
}