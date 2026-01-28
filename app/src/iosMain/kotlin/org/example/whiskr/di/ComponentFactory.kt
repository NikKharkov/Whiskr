package org.example.whiskr.di

import org.example.whiskr.DatabaseFactory
import org.example.whiskr.domain.ShareService
import org.example.whiskr.share.ImageShareManager

expect fun createIosModule(
    databaseFactory: DatabaseFactory,
    shareService: ShareService,
    imageShareManager: ImageShareManager
): IosApplicationModule
