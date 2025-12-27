package org.example.whiskr.di

import android.content.Context
import com.liftric.kvault.KVault
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.example.whiskr.util.BUNDLE

@Singleton
@Component
abstract class AndroidApplicationComponentDI(
    @get:Provides val context: Context,
) : ApplicationComponentDI {
    @Provides
    @Singleton
    fun provideKVault(context: Context): KVault = KVault(context, BUNDLE)

    companion object
}
