package org.example.whiskr.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.AuthFlowComponent
import org.example.whiskr.TokenStorage
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.component.componentScope
import org.example.whiskr.preferences.UserPreferences

@Inject
class DefaultRootComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val deepLink: String?,
    private val tokenStorage: TokenStorage,
    private val userPreferences: UserPreferences,
    private val authFlowFactory: AuthFlowComponent.Factory,
    private val mainFlowFactory: MainFlowComponent.Factory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<RootComponent.Config>()
    private val scope = componentScope()

    private val _isDarkTheme = MutableValue(userPreferences.isDarkTheme)
    override val isDarkTheme: Value<Boolean> = _isDarkTheme

    private val startConfig: RootComponent.Config = if (tokenStorage.isUserLoggedIn) {
        RootComponent.Config.MainFlow
    } else {
        RootComponent.Config.AuthFlow
    }

    override val stack = childStack(
        source = navigation,
        serializer = RootComponent.Config.serializer(),
        initialConfiguration = startConfig,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        scope.launch { userPreferences.setDarkTheme(isDark) }
    }

    override fun onDeepLink(url: String) {
        val activeChild = stack.value.active.instance
        when (activeChild) {
            is RootComponent.Child.MainFlow -> {
                activeChild.component.onDeepLink(url)
            }

            is RootComponent.Child.AuthFlow -> {

            }
        }
    }

    private fun createChild(
        config: RootComponent.Config,
        context: ComponentContext
    ): RootComponent.Child = when (config) {

        RootComponent.Config.AuthFlow -> RootComponent.Child.AuthFlow(
            authFlowFactory(
                componentContext = context,
                onAuthSuccess = {
                    navigation.replaceAll(RootComponent.Config.MainFlow)
                }
            )
        )

        RootComponent.Config.MainFlow -> RootComponent.Child.MainFlow(
            mainFlowFactory(
                componentContext = context,
                isDarkTheme = isDarkTheme,
                deepLink = deepLink
            )
        )
    }
}