package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import component.pet.PetRegistrationComponent
import component.user.UserRegistrationComponent
import kotlinx.serialization.Serializable

interface RegistrationWizardComponent {
    val stack: Value<ChildStack<Config, Child>>

    sealed class Child {
        class UserStep(val component: UserRegistrationComponent) : Child()
        class PetStep(val component: PetRegistrationComponent) : Child()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object UserStep : Config

        @Serializable
        data object PetStep : Config
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onWizardFinished: () -> Unit
        ): RegistrationWizardComponent
    }
}