package org.example.whiskr.di

import org.example.whiskr.DatabaseFactory
import org.example.whiskr.domain.ShareService

actual fun createIosModule(
    databaseFactory: DatabaseFactory,
    shareService: ShareService
): IosApplicationModule = InjectIosApplicationModule(databaseFactory, shareService)