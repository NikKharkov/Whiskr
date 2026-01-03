package org.example.whiskr

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.root.RootComponent
import org.example.whiskr.theme.ThemeRevealContainer
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun RootContent(rootComponent: RootComponent) {
    val isDarkTheme by rootComponent.isDarkTheme.subscribeAsState()

    ThemeRevealContainer(
        isDarkTheme = isDarkTheme,
        onToggleTheme = { newTheme ->
            rootComponent.toggleTheme(newTheme)
        }
    ) { isDarkTheme, startAnimation ->
        WhiskrTheme(isDarkTheme = isDarkTheme) {
            Children(stack = rootComponent.stack) { child ->
                when (val instance = child.instance) {
                    is RootComponent.Child.AuthFlow -> {
                        AuthFlowContent(component = instance.component)
                    }

                    is RootComponent.Child.MainFlow -> {
                        MainFlowContent(
                            component = instance.component,
                            isDarkThemeOverride = isDarkTheme,
                            onThemeAnimationStart = startAnimation
                        )
                    }
                }
            }
        }
    }
}