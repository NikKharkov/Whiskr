package org.example.whiskr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.RegistrationWizardContent
import org.example.whiskr.ui.LoginScreen
import org.example.whiskr.ui.VerifyScreen
import org.example.whiskr.ui.WelcomeScreen

@Composable
fun AuthFlowContent(component: AuthFlowComponent) {
    val dialogSlot by component.dialogSlot.subscribeAsState()

    Children(stack = component.stack) { child ->
        when (val instance = child.instance) {
            is AuthFlowComponent.Child.Welcome -> {
                WelcomeScreen(component = instance.component)
            }
        }
    }

    dialogSlot.child?.instance?.let { child ->
        when (child) {
            is AuthFlowComponent.DialogChild.Login -> {
                LoginScreen(component = child.component)
            }
            is AuthFlowComponent.DialogChild.Verification -> {
                VerifyScreen(component = child.component)
            }
            is AuthFlowComponent.DialogChild.RegistrationWizard -> {
                RegistrationWizardContent(component = child.component)
            }
        }
    }
}