package org.example.whiskr.di

import org.example.whiskr.DatabaseFactory
import org.example.whiskr.domain.ShareService

expect fun createIosComponent(
    databaseFactory: DatabaseFactory,
    shareService: ShareService
): IosApplicationComponentDI
