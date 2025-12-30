package org.example.whiskr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.RegistrationWizardContent
import org.example.whiskr.root.RootComponent
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.LoginScreen
import org.example.whiskr.ui.VerifyScreen
import org.example.whiskr.ui.WelcomeScreen
import ui.MainScreen

@Composable
fun RootContent(rootComponent: RootComponent) {
    WhiskrTheme(isDarkTheme = false) {
        val dialogSlot by rootComponent.dialogSlot.subscribeAsState()

        Children(stack = rootComponent.stack) { child ->
            when (val instance = child.instance) {
                is RootComponent.StackChild.Welcome -> {
                    WelcomeScreen(welcomeComponent = instance.welcomeComponent)
                }

                is RootComponent.StackChild.Main -> {
                    MainScreen(mainComponent = instance.mainComponent)
                }
            }
        }

        dialogSlot.child?.instance?.let { child ->
            when (child) {
                is RootComponent.DialogChild.Login -> LoginScreen(child.loginComponent)
                is RootComponent.DialogChild.Verification -> VerifyScreen(child.verificationComponent)
                is RootComponent.DialogChild.RegistrationWizard -> RegistrationWizardContent(child.registrationComponent)
            }
        }
    }
}
