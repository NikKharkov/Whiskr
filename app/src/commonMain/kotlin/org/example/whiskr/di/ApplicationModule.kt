package org.example.whiskr.di

import di.AddEditModule
import di.ChatModule
import di.ProfileModule
import di.RegistrationModule
import di.UserModule
import org.example.whiskr.AuthFlowModule
import org.example.whiskr.NetworkModule
import org.example.whiskr.root.RootComponent
import org.example.whiskr.root.RootModule

interface ApplicationModule :
    NetworkModule, AuthModule, RootModule, RegistrationModule,
    HomeModule, PostModule, AuthFlowModule, MainFlowModule, UserModule,
    StorageModule, MediaViewerModule, BillingModule, AiModule, ProfileModule,
    ExploreModule, AddEditModule, NotificationModule, ChatModule {
    val rootComponentFactory: RootComponent.Factory
}
