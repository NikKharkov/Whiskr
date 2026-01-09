package org.example.whiskr.di

import org.example.whiskr.domain.MediaProcessingService
import org.example.whiskr.DatabaseFactory

actual fun createIosComponent(
    databaseFactory: DatabaseFactory,
    mediaProcessingService: MediaProcessingService
): IosApplicationComponentDI = InjectIosApplicationComponentDI(databaseFactory, mediaProcessingService)