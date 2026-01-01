package org.example.whiskr

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import component.RegistrationWizardComponent
import kotlinx.serialization.Serializable
import org.example.whiskr.component.login.LoginComponent
import org.example.whiskr.component.verification.VerificationComponent
import org.example.whiskr.component.welcome.WelcomeComponent

interface AuthFlowComponent {

    val stack: Value<ChildStack<Config, Child>>
    val dialogSlot: Value<ChildSlot<DialogConfig, DialogChild>>

    @Serializable
    sealed interface Config {
        @Serializable
        data object Welcome : Config
    }

    @Serializable
    sealed interface DialogConfig {
        @Serializable
        data object Login : DialogConfig
        @Serializable
        data class Verification(val email: String) : DialogConfig
        @Serializable
        data object RegistrationWizard : DialogConfig
    }

    sealed interface Child {
        class Welcome(val component: WelcomeComponent) : Child
    }

    sealed interface DialogChild {
        class Login(val component: LoginComponent) : DialogChild
        class Verification(val component: VerificationComponent) : DialogChild
        class RegistrationWizard(val component: RegistrationWizardComponent) : DialogChild
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onAuthSuccess: () -> Unit
        ): AuthFlowComponent
    }
}