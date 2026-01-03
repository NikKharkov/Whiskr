package org.example.whiskr

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.ui.AdaptiveMainLayout
import org.example.whiskr.util.toTab
import ui.HomeScreen

@Composable
fun MainFlowContent(
    component: MainFlowComponent,
    isDarkThemeOverride: Boolean? = null,
    onThemeAnimationStart: (Offset) -> Unit
) {
    val stack by component.stack.subscribeAsState()
    val user by component.userState.subscribeAsState()
    val componentTheme by component.isDarkTheme.subscribeAsState()
    val isDarkTheme = isDarkThemeOverride ?: componentTheme

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp

        AdaptiveMainLayout(
            isTablet = isTablet,
            activeTab = stack.active.instance.toTab(),
            userState = user,
            isDarkTheme = isDarkTheme,
            onThemeToggle = { centerOffset ->
                onThemeAnimationStart(centerOffset)
            },
            onTabSelected = component::onTabSelected,
        ) {
            Children(
                stack = stack,
                modifier = Modifier.fillMaxSize()
            ) {
                when (val child = it.instance) {
                    is MainFlowComponent.Child.Home -> HomeScreen(child.component)
                    is MainFlowComponent.Child.Explore -> Text("Explore screen")
                    is MainFlowComponent.Child.AIStudio -> Text("AI Studio screen")
                    is MainFlowComponent.Child.Games -> Text("Games screen")
                    is MainFlowComponent.Child.Messages -> Text("Messages screen")
                    is MainFlowComponent.Child.Profile -> Text("Profile screen")
                }
            }
        }
    }
}