package org.example.whiskr.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import component.MainComponent
import component.RegistrationWizardComponent
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.login.LoginComponent
import org.example.whiskr.component.verification.VerificationComponent
import org.example.whiskr.component.welcome.WelcomeComponent
import org.example.whiskr.domain.AuthRepository

@Inject
class DefaultRootComponent(
    private val welcomeFactory: WelcomeComponent.Factory,
    private val loginFactory: LoginComponent.Factory,
    private val verificationFactory: VerificationComponent.Factory,
    private val registrationFactory: RegistrationWizardComponent.Factory,
    private val mainFactory: MainComponent.Factory,
    private val authRepository: AuthRepository,
    @Assisted private val componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<RootComponent.StackConfig>()
    private val dialogNavigation = SlotNavigation<RootComponent.DialogConfig>()

    private val startConfig =
        if (authRepository.isUserLoggedIn()) {
            RootComponent.StackConfig.Main
        } else {
            RootComponent.StackConfig.Welcome
        }

    override val stack =
        childStack(
            source = navigation,
            serializer = RootComponent.StackConfig.serializer(),
            initialConfiguration = startConfig,
            handleBackButton = true,
            childFactory = ::createStackChild
        )

    override val dialogSlot =
        childSlot(
            source = dialogNavigation,
            serializer = RootComponent.DialogConfig.serializer(),
            handleBackButton = true,
            childFactory = ::createDialogChild,
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun createStackChild(
        config: RootComponent.StackConfig,
        componentContext: ComponentContext,
    ): RootComponent.StackChild =
        when (config) {
            RootComponent.StackConfig.Welcome ->
                RootComponent.StackChild.Welcome(
                    welcomeFactory(
                        componentContext = componentContext,
                        onNavigateToLogin = {
                             dialogNavigation.activate(RootComponent.DialogConfig.Login)
                        },
                        onNavigateToMain = { navigation.replaceAll(RootComponent.StackConfig.Main) },
                    )
                )

            RootComponent.StackConfig.Main ->
                RootComponent.StackChild.Main(
                    mainComponent =
                        mainFactory(
                            componentContext = componentContext,
                            onSignOut = {
                                navigation.replaceAll(RootComponent.StackConfig.Welcome)
                            },
                            onProfileMissing = {
                                navigation.replaceAll(RootComponent.StackConfig.Welcome)
                                dialogNavigation.activate(RootComponent.DialogConfig.RegistrationWizard)
                            }
                        )
                )
        }

    private fun createDialogChild(
        config: RootComponent.DialogConfig,
        componentContext: ComponentContext,
    ): RootComponent.DialogChild {
        return when (config) {
            RootComponent.DialogConfig.Login ->
                RootComponent.DialogChild.Login(
                    loginFactory(
                        componentContext = componentContext,
                        onCloseClick = { dialogNavigation.dismiss() },
                        onNavigateToVerify = { email ->
                            dialogNavigation.activate(RootComponent.DialogConfig.Verification(email))
                        },
                    )
                )

            is RootComponent.DialogConfig.Verification ->
                RootComponent.DialogChild.Verification(
                    verificationFactory(
                        componentContext = componentContext,
                        email = config.email,
                        onVerified = { isNewUser ->
                            if (isNewUser) {
                                dialogNavigation.activate(RootComponent.DialogConfig.RegistrationWizard)
                            } else {
                                navigation.replaceAll(RootComponent.StackConfig.Main)
                                dialogNavigation.dismiss()
                            }
                        },
                        onBack = { dialogNavigation.activate(RootComponent.DialogConfig.Login) },
                    )
                )

            RootComponent.DialogConfig.RegistrationWizard ->
                RootComponent.DialogChild.RegistrationWizard(
                    registrationFactory(
                        componentContext = componentContext,
                        onWizardFinished = {
                            navigation.replaceAll(RootComponent.StackConfig.Main)
                            dialogNavigation.dismiss()
                        }
                    )
                )
        }
    }
}
