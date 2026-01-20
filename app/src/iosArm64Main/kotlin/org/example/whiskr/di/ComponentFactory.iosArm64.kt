package org.example.whiskr.di

import org.example.whiskr.DatabaseFactory
import org.example.whiskr.domain.ShareService

actual fun createIosComponent(
    databaseFactory: DatabaseFactory,
    shareService: ShareService
): IosApplicationComponentDI = InjectIosApplicationComponentDI(databaseFactory, shareService)