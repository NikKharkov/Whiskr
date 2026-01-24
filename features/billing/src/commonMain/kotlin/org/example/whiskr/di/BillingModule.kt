package org.example.whiskr.di

import com.arkivanov.decompose.ComponentContext
import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.component.DefaultStoreComponent
import org.example.whiskr.component.StoreComponent
import org.example.whiskr.data.BillingApiService
import org.example.whiskr.data.BillingRepositoryImpl
import org.example.whiskr.data.createBillingApiService
import org.example.whiskr.domain.BillingRepository

interface BillingModule {

    @Provides
    @Singleton
    fun provideBillingApiService(ktorfit: Ktorfit): BillingApiService = ktorfit.createBillingApiService()

    @Provides
    @Singleton
    fun provideBillingRepository(
        billingApiService: BillingApiService
    ): BillingRepository = BillingRepositoryImpl(billingApiService)

    @Provides
    @Singleton
    fun provideStoreFactory(
        factory: (ComponentContext, () -> Unit) -> DefaultStoreComponent
    ): StoreComponent.Factory {
        return StoreComponent.Factory(factory)
    }
}