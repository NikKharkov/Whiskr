package org.example.whiskr.di

import org.example.whiskr.domain.MediaProcessingService
import org.example.whiskr.DatabaseFactory

expect fun createIosComponent(
    databaseFactory: DatabaseFactory,
    mediaProcessingService: MediaProcessingService
): IosApplicationComponentDI
