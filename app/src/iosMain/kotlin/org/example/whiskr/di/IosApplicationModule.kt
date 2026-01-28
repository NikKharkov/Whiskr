package org.example.whiskr.di

import com.liftric.kvault.KVault
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.DatabaseFactory
import org.example.whiskr.domain.ShareService
import org.example.whiskr.share.ImageShareManager
import org.example.whiskr.util.BUNDLE

@Singleton
@Component
abstract class IosApplicationModule(
    @get:Provides val databaseFactory: DatabaseFactory,
    @get:Provides val shareService: ShareService,
    @get:Provides val imageShareManager: ImageShareManager
) : ApplicationModule {
    @Provides
    @Singleton
    fun provideKVault(): KVault = KVault(BUNDLE)

    companion object
}
