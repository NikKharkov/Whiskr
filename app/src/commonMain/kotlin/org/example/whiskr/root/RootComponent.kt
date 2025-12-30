package org.example.whiskr.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import component.MainComponent
import component.RegistrationWizardComponent
import kotlinx.serialization.Serializable
import org.example.whiskr.component.login.LoginComponent
import org.example.whiskr.component.verification.VerificationComponent
import org.example.whiskr.component.welcome.WelcomeComponent

interface RootComponent {
    val stack: Value<ChildStack<StackConfig, StackChild>>
    val dialogSlot: Value<ChildSlot<DialogConfig, DialogChild>>

    @Serializable
    sealed interface StackConfig {
        @Serializable
        data object Welcome : StackConfig

        @Serializable
        data object Main : StackConfig
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

    sealed interface StackChild {
        class Welcome(val welcomeComponent: WelcomeComponent) : StackChild
        class Main(val mainComponent: MainComponent) : StackChild
    }

    sealed interface DialogChild {
        class Login(val loginComponent: LoginComponent) : DialogChild
        class Verification(val verificationComponent: VerificationComponent) : DialogChild
        class RegistrationWizard(val registrationComponent: RegistrationWizardComponent) : DialogChild
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RootComponent
    }
}
