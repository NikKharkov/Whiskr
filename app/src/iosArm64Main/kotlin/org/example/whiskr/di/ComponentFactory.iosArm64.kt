package org.example.whiskr.di

import com.russhwolf.settings.ObservableSettings
import org.example.whiskr.DatabaseFactory

actual fun createIosComponent(
    databaseFactory: DatabaseFactory
): IosApplicationComponentDI = InjectIosApplicationComponentDI(databaseFactory)