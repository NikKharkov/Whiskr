package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.TokenStorage
import org.example.whiskr.component.login.DefaultLoginComponent
import org.example.whiskr.component.login.LoginComponent
import org.example.whiskr.component.verification.DefaultVerificationComponent
import org.example.whiskr.component.verification.VerificationComponent
import org.example.whiskr.component.welcome.DefaultWelcomeComponent
import org.example.whiskr.component.welcome.WelcomeComponent
import org.example.whiskr.data.AuthApiService
import org.example.whiskr.data.AuthRepositoryImpl
import org.example.whiskr.data.createAuthApiService
import org.example.whiskr.domain.AuthRepository

interface AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApiService,
        tokenStorage: TokenStorage,
    ): AuthRepository = AuthRepositoryImpl(authApi, tokenStorage)

    @Provides
    @Singleton
    fun provideAuthApiService(ktorfit: Ktorfit): AuthApiService = ktorfit.createAuthApiService()

    @Provides
    @Singleton
    fun provideWelcomeFactory(factory: (ComponentContext, () -> Unit, () -> Unit) -> DefaultWelcomeComponent): WelcomeComponent.Factory {
        return WelcomeComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideLoginFactory(factory: (ComponentContext, () -> Unit, (String) -> Unit) -> DefaultLoginComponent): LoginComponent.Factory {
        return LoginComponent.Factory(factory)
    }

    @Provides
    @Singleton
    fun provideVerificationFactory(
        factory: (ComponentContext, String, () -> Unit, (Boolean) -> Unit) -> DefaultVerificationComponent,
    ): VerificationComponent.Factory {
        return VerificationComponent.Factory(factory)
    }
}
