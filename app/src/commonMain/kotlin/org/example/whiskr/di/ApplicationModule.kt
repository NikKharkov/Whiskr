package org.example.whiskr.di

import di.RegistrationModule
import di.UserModule
import org.example.whiskr.AuthFlowModule
import org.example.whiskr.NetworkModule
import org.example.whiskr.root.RootComponent
import org.example.whiskr.root.RootModule

interface ApplicationModule :
    NetworkModule, AuthModule, RootModule, RegistrationModule,
    PostModule, AuthFlowModule, MainFlowModule, UserModule,
    StorageModule, MediaViewerModule, BillingModule, AiModule {
    val rootComponentFactory: RootComponent.Factory
}
