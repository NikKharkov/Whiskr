package org.example.whiskr.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.example.whiskr.AuthFlowComponent
import org.example.whiskr.component.MainFlowComponent

interface RootComponent {
    val stack: Value<ChildStack<Config, Child>>

    @Serializable
    sealed interface Config {
        @Serializable data object AuthFlow : Config
        @Serializable data object MainFlow : Config
    }

    sealed interface Child {
        class AuthFlow(val component: AuthFlowComponent) : Child
        class MainFlow(val component: MainFlowComponent) : Child
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RootComponent
    }
}