package org.example.whiskr.di

import com.liftric.kvault.KVault
import org.example.whiskr.domain.MediaProcessingService
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.DatabaseFactory
import org.example.whiskr.util.BUNDLE

@Singleton
@Component
abstract class IosApplicationComponentDI(
    @get:Provides val databaseFactory: DatabaseFactory,
    @get:Provides val mediaProcessingService: MediaProcessingService
) : ApplicationComponentDI {
    @Provides
    @Singleton
    fun provideKVault(): KVault = KVault(BUNDLE)

    companion object
}
