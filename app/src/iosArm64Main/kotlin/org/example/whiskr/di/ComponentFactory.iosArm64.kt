package org.example.whiskr.di

import org.example.whiskr.DatabaseFactory

actual fun createIosComponent(
    databaseFactory: DatabaseFactory,
): IosApplicationComponentDI = InjectIosApplicationComponentDI(databaseFactory)