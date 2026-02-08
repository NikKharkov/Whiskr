package di

import com.arkivanov.decompose.ComponentContext
import component.DefaultProfileComponent
import component.ProfileComponent
import org.example.whiskr.dto.PetResponse
import data.ProfileApiService
import data.ProfileRepositoryImpl
import data.createProfileApiService
import de.jensklingenberg.ktorfit.Ktorfit
import domain.ProfileRepository
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.data.Post
import org.example.whiskr.data.Media
import org.example.whiskr.di.Singleton

interface ProfileModule {

    @Provides
    @Singleton
    fun provideProfileApiService(ktorfit: Ktorfit): ProfileApiService = ktorfit.createProfileApiService()

    @Provides
    @Singleton
    fun provideProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository = profileRepositoryImpl

    @Provides
    @Singleton
    fun provideProfileComponentFactory(
        factory: (
            ComponentContext,
            String, // handle
            () -> Unit, // onBack
            (Post) -> Unit, // onNavPost
            (String) -> Unit, // onNavUser
            (List<Media>, Int) -> Unit, // onNavMedia
            (String) -> Unit, // onNavTag,
            (Post) -> Unit, // onRepost
            () -> Unit, // editProfile
            () -> Unit, // addPet
            (Long, PetResponse) -> Unit, // editPet
            (Long) -> Unit, // onMessage
        ) -> DefaultProfileComponent
    ): ProfileComponent.Factory {
        return ProfileComponent.Factory(factory)
    }
}