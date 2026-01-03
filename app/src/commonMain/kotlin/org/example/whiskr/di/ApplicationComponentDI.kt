package org.example.whiskr.di

import di.HomeComponentDI
import di.RegistrationComponent
import di.UserComponentDI
import org.example.whiskr.AuthFlowComponentDI
import org.example.whiskr.NetworkComponent
import org.example.whiskr.root.RootComponent
import org.example.whiskr.root.RootComponentDI

interface ApplicationComponentDI :
    NetworkComponent, AuthComponent, RootComponentDI, RegistrationComponent,
    HomeComponentDI, AuthFlowComponentDI, MainFlowComponentDI, UserComponentDI,
    StorageComponent {
    val rootComponentFactory: RootComponent.Factory
}
