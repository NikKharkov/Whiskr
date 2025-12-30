package component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ui.PetRegistrationScreen
import ui.UserRegistrationScreen

@Composable
fun RegistrationWizardContent(component: RegistrationWizardComponent) {
    val stack by component.stack.subscribeAsState()

    Children(stack = stack) { child ->
        when (val instance = child.instance) {
            is RegistrationWizardComponent.Child.UserStep ->
                UserRegistrationScreen(instance.component)
            is RegistrationWizardComponent.Child.PetStep ->
                PetRegistrationScreen(instance.component)
        }
    }
}