package di

import com.arkivanov.decompose.ComponentContext
import component.add_pet.AddPetComponent
import component.add_pet.DefaultAddPetComponent
import component.edit_pet.DefaultEditPetComponent
import component.edit_pet.EditPetComponent
import component.edit_profile.DefaultEditProfileComponent
import component.edit_profile.EditProfileComponent
import data.AddEditApiService
import data.AddEditRepositoryImpl
import data.createAddEditApiService
import org.example.whiskr.dto.PetResponse
import de.jensklingenberg.ktorfit.Ktorfit
import domain.AddEditRepository
import domain.UserState
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.di.Singleton

interface AddEditModule {

    @Provides
    @Singleton
    fun provideAddEditApiService(ktorfit: Ktorfit): AddEditApiService = ktorfit.createAddEditApiService()

    @Provides
    @Singleton
    fun provideAddEditRepository(addEditApiService: AddEditApiService): AddEditRepository =
        AddEditRepositoryImpl(addEditApiService)

    @Provides
    @Singleton
    fun provideEditProfileComponentFactory(
        factory: (
            ComponentContext,
            UserState, // initialProfile
            () -> Unit, // onBack
            () -> Unit  // onProfileUpdated
        ) -> DefaultEditProfileComponent
    ): EditProfileComponent.Factory {
        return EditProfileComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideAddPetComponentFactory(
        factory: (
            ComponentContext,
            () -> Unit, // onBack
            () -> Unit  // onPetAdded
        ) -> DefaultAddPetComponent
    ): AddPetComponent.Factory {
        return AddPetComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideEditPetComponentFactory(
        factory: (
            ComponentContext,
            Long,        // petId
            PetResponse, // initialPetData
            () -> Unit,  // onBack
            () -> Unit   // onPetUpdated
        ) -> DefaultEditPetComponent
    ): EditPetComponent.Factory {
        return EditPetComponent.Factory(factory)
    }

}