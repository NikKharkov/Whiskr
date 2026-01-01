package org.example.whiskr

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import component.RegistrationWizardComponent
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.login.LoginComponent
import org.example.whiskr.component.verification.VerificationComponent
import org.example.whiskr.component.welcome.WelcomeComponent

@Inject
class DefaultAuthFlowComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onAuthSuccess: () -> Unit,
    private val welcomeFactory: WelcomeComponent.Factory,
    private val loginFactory: LoginComponent.Factory,
    private val verificationFactory: VerificationComponent.Factory,
    private val registrationFactory: RegistrationWizardComponent.Factory,
) : AuthFlowComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<AuthFlowComponent.Config>()
    private val dialogNavigation = SlotNavigation<AuthFlowComponent.DialogConfig>()

    override val stack = childStack(
        source = navigation,
        serializer = AuthFlowComponent.Config.serializer(),
        initialConfiguration = AuthFlowComponent.Config.Welcome,
        childFactory = ::createChild
    )

    override val dialogSlot = childSlot(
        source = dialogNavigation,
        serializer = AuthFlowComponent.DialogConfig.serializer(),
        childFactory = ::createDialogChild
    )

    private fun createChild(config: AuthFlowComponent.Config, context: ComponentContext): AuthFlowComponent.Child =
        when (config) {
            AuthFlowComponent.Config.Welcome -> AuthFlowComponent.Child.Welcome(
                welcomeFactory(
                    componentContext = context,
                    onNavigateToLogin = { dialogNavigation.activate(AuthFlowComponent.DialogConfig.Login) },
                    onNavigateToMain = onAuthSuccess
                )
            )
        }

    private fun createDialogChild(config: AuthFlowComponent.DialogConfig, context: ComponentContext): AuthFlowComponent.DialogChild =
        when (config) {
            AuthFlowComponent.DialogConfig.Login -> AuthFlowComponent.DialogChild.Login(
                loginFactory(
                    componentContext = context,
                    onCloseClick = { dialogNavigation.dismiss() },
                    onNavigateToVerify = { email ->
                        dialogNavigation.activate(AuthFlowComponent.DialogConfig.Verification(email))
                    }
                )
            )
            is AuthFlowComponent.DialogConfig.Verification -> AuthFlowComponent.DialogChild.Verification(
                verificationFactory(
                    componentContext = context,
                    email = config.email,
                    onVerified = { isNewUser ->
                        if (isNewUser) {
                            dialogNavigation.activate(AuthFlowComponent.DialogConfig.RegistrationWizard)
                        } else {
                            onAuthSuccess()
                        }
                    },
                    onBack = { dialogNavigation.activate(AuthFlowComponent.DialogConfig.Login) }
                )
            )
            AuthFlowComponent.DialogConfig.RegistrationWizard -> AuthFlowComponent.DialogChild.RegistrationWizard(
                registrationFactory(
                    componentContext = context,
                    onWizardFinished = onAuthSuccess
                )
            )
        }
}