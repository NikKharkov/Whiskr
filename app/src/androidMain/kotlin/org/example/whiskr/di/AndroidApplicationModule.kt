package org.example.whiskr.di

import android.content.Context
import com.liftric.kvault.KVault
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.DatabaseFactory
import org.example.whiskr.domain.ShareService
import org.example.whiskr.share.ImageShareManager
import org.example.whiskr.util.BUNDLE

@Singleton
@Component
abstract class AndroidApplicationModule(
    @get:Provides val context: Context,
    @get:Provides val databaseFactory: DatabaseFactory,
    @get:Provides val shareService: ShareService,
    @get:Provides val imageShareManager: ImageShareManager
) : ApplicationModule {
    @Provides
    @Singleton
    fun provideKVault(context: Context): KVault = KVault(context, BUNDLE)

    companion object
}
