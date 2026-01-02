package di

import data.UserApiService
import data.UserRepositoryImpl
import data.createUserApiService
import de.jensklingenberg.ktorfit.Ktorfit
import domain.UserRepository
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.di.Singleton

interface UserComponentDI {

    @Provides
    @Singleton
    fun provideUserRepository(profileApi: UserApiService): UserRepository =
        UserRepositoryImpl(profileApi)

    @Provides
    @Singleton
    fun provideMainApiService(ktorfit: Ktorfit): UserApiService = ktorfit.createUserApiService()
}