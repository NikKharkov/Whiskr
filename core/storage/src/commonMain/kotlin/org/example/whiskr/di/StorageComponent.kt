package org.example.whiskr.di

import me.tatarka.inject.annotations.Provides
import org.example.whiskr.AppDatabase
import org.example.whiskr.DatabaseFactory
import org.example.whiskr.database.daos.UserDao
import org.example.whiskr.preferences.UserPreferences

interface StorageComponent {

    @Singleton
    @Provides
    fun provideAppDatabase(
        platformFactory: DatabaseFactory
    ): AppDatabase {
        return platformFactory.create()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Singleton
    @Provides
    fun provideUserPreferences(): UserPreferences =
        UserPreferences()
}