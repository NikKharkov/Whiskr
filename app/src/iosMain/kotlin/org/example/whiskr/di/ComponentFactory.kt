package org.example.whiskr.di

import com.russhwolf.settings.ObservableSettings
import org.example.whiskr.DatabaseFactory

expect fun createIosComponent(
    databaseFactory: DatabaseFactory,
    settings: ObservableSettings
): IosApplicationComponentDI
