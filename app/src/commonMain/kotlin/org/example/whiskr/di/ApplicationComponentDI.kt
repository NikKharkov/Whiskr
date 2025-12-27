package org.example.whiskr.di

import di.MainComponentDI
import di.RegistrationComponent
import org.example.whiskr.NetworkComponent
import org.example.whiskr.root.RootComponent
import org.example.whiskr.root.RootComponentDI

interface ApplicationComponentDI :
    NetworkComponent,
    AuthComponent,
    RootComponentDI,
    RegistrationComponent,
    MainComponentDI {
    val rootComponentFactory: RootComponent.Factory
}
