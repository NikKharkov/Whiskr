package di

import data.UserApiService
import data.UserRepositoryImpl
import data.createUserApiService
import de.jensklingenberg.ktorfit.Ktorfit
import domain.UserRepository
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.database.daos.UserDao
import org.example.whiskr.di.Singleton

interface UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        profileApi: UserApiService,
        userDao: UserDao
    ): UserRepository =
        UserRepositoryImpl(profileApi, userDao)

    @Provides
    @Singleton
    fun provideMainApiService(ktorfit: Ktorfit): UserApiService = ktorfit.createUserApiService()
}