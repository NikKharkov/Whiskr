package org.example.whiskr

import com.arkivanov.decompose.ComponentContext

interface MainFlowComponent {


    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onSignOut: () -> Unit
        ): MainFlowComponent
    }
}