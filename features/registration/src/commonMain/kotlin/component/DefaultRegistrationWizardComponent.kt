package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import component.pet.PetRegistrationComponent
import component.user.UserRegistrationComponent
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class DefaultRegistrationWizardComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onWizardFinished: () -> Unit,
    private val userRegFactory: UserRegistrationComponent.Factory,
    private val petRegFactory: PetRegistrationComponent.Factory
) : RegistrationWizardComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<RegistrationWizardComponent.Config>()

    override val stack = childStack(
        source = navigation,
        serializer = RegistrationWizardComponent.Config.serializer(),
        initialConfiguration = RegistrationWizardComponent.Config.UserStep,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: RegistrationWizardComponent.Config,
        context: ComponentContext
    ): RegistrationWizardComponent.Child =
        when (config) {
            RegistrationWizardComponent.Config.UserStep -> RegistrationWizardComponent.Child.UserStep(
                userRegFactory(
                    componentContext = context,
                    onNextClicked = { navigation.push(RegistrationWizardComponent.Config.PetStep) }
                )
            )

            RegistrationWizardComponent.Config.PetStep -> RegistrationWizardComponent.Child.PetStep(
                petRegFactory(
                    componentContext = context,
                    onFinished = onWizardFinished,
                    onBack = { navigation.pop() }
                )
            )
        }
}