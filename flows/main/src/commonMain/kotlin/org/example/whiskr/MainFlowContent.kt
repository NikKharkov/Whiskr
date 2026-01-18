package org.example.whiskr

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.ui.AdaptiveMainLayout
import org.example.whiskr.ui.CreatePostScreen
import org.example.whiskr.ui.CreateReplyScreen
import org.example.whiskr.ui.HomeScreen
import org.example.whiskr.ui.MediaViewerScreen
import org.example.whiskr.ui.PostDetailsScreen
import org.example.whiskr.util.showsNavigation
import org.example.whiskr.util.toTab
import util.LocalUser

@Composable
fun MainFlowContent(
    component: MainFlowComponent,
    isDarkTheme: Boolean,
    onThemeAnimationStart: (Offset) -> Unit
) {
    val stack by component.stack.subscribeAsState()
    val user by component.userState.subscribeAsState()
    val isDrawerOpen by component.isDrawerOpen.subscribeAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 960.dp

        CompositionLocalProvider(LocalIsTablet provides isTablet) {
            CompositionLocalProvider(LocalUser provides user) {

                AdaptiveMainLayout(
                    activeTab = stack.active.instance.toTab(),
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { centerOffset ->
                        onThemeAnimationStart(centerOffset)
                    },
                    onTabSelected = component::onTabSelected,
                    isDrawerOpen = isDrawerOpen,
                    shouldShowNavigation = stack.active.instance.showsNavigation,
                    onPostClick = component::onPostClick,
                    onDrawerOpenChange = component::setDrawerOpen
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
                            is MainFlowComponent.Child.CreatePost -> CreatePostScreen(child.component)
                            is MainFlowComponent.Child.CreateReply -> CreateReplyScreen(child.component)
                            is MainFlowComponent.Child.PostDetails -> PostDetailsScreen(child.component)
                            is MainFlowComponent.Child.MediaViewer -> MediaViewerScreen(child.component)
                        }
                    }
                }
            }
        }
    }
}