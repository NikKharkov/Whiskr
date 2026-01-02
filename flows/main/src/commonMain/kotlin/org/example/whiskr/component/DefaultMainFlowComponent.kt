package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import component.HomeComponent
import domain.UserRepository
import domain.UserState
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.util.toConfig

@OptIn(DelicateDecomposeApi::class)
@Inject
class DefaultMainFlowComponent(
    @Assisted private val componentContext: ComponentContext,
    @Assisted private val onSignOut: () -> Unit,
    private val userRepository: UserRepository,
    private val homeFactory: HomeComponent.Factory
) : MainFlowComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<MainFlowComponent.Config>()
    private val scope = componentScope()
    override val userState: Value<UserState> = userRepository.user

    init {
        scope.launch {
            if (userRepository.user.value.profile == null) {
                userRepository.getMyProfile()
            }
        }
    }

    override val stack = childStack(
        source = navigation,
        serializer = MainFlowComponent.Config.serializer(),
        initialConfiguration = MainFlowComponent.Config.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: MainFlowComponent.Config,
        context: ComponentContext
    ): MainFlowComponent.Child = when (config) {
        MainFlowComponent.Config.Home -> MainFlowComponent.Child.Home(
            homeFactory(
                componentContext = context,
                onSignOut = onSignOut,
                onRefresh = { userRepository.getMyProfile() }
            )
        )

        MainFlowComponent.Config.AIStudio -> TODO()
        MainFlowComponent.Config.Explore -> TODO()
        MainFlowComponent.Config.Games -> TODO()
        MainFlowComponent.Config.Messages -> TODO()
        MainFlowComponent.Config.Profile -> TODO()
    }

    override fun onTabSelected(tab: MainFlowComponent.Tab) {
        navigation.bringToFront(tab.toConfig())
    }
}