package org.example.whiskr

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.example.whiskr.root.RootComponent
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun RootContent(rootComponent: RootComponent) {
    WhiskrTheme(isDarkTheme = false) {
        Children(stack = rootComponent.stack) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.AuthFlow -> {
                    AuthFlowContent(component = instance.component)
                }

                is RootComponent.Child.MainFlow -> {
                    MainFlowContent(component = instance.component)
                }
            }
        }
    }
}