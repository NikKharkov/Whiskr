package org.example.whiskr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.root.RootComponent
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.LoginScreen
import org.example.whiskr.ui.VerifyScreen
import org.example.whiskr.ui.WelcomeScreen
import ui.MainScreen
import ui.UserRegistrationScreen

@Composable
fun RootContent(rootComponent: RootComponent) {
    WhiskrTheme(isDarkTheme = false) {
        val dialogSlot by rootComponent.dialogSlot.subscribeAsState()

        Children(
            stack = rootComponent.stack,
            animation = stackAnimation(fade()),
        ) { child ->
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
                is RootComponent.DialogChild.Registration -> UserRegistrationScreen(child.userRegistrationComponent)
            }
        }
    }
}
