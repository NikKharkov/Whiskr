package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.MobileBottomBar
import org.example.whiskr.ui.components.MobileDrawerContent
import org.example.whiskr.ui.components.MobileTopBar
import org.example.whiskr.ui.components.TabletSidebar

@Composable
fun AdaptiveMainLayout(
    modifier: Modifier = Modifier,
    activeTab: MainFlowComponent.Tab?,
    onTabSelected: (MainFlowComponent.Tab) -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: (Offset) -> Unit,
    isDrawerOpen: Boolean,
    onDrawerOpenChange: (Boolean) -> Unit,
    onPostClick: () -> Unit,
    shouldShowNavigation: Boolean,
    content: @Composable () -> Unit
) {
    val isTablet = LocalIsTablet.current

    if (isTablet) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(WhiskrTheme.colors.background)
        ) {
            TabletSidebar(
                activeTab = activeTab,
                onTabSelected = onTabSelected,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onPostClick = onPostClick,
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .fillMaxHeight()
                    .windowInsetsPadding(WindowInsets.displayCutout)
                    .systemBarsPadding()
                    .padding(vertical = 24.dp)
            )

            VerticalDivider(color = WhiskrTheme.colors.surface)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .windowInsetsPadding(WindowInsets.displayCutout)
            ) {
                content()
            }
        }
    } else {
        val drawerState = rememberDrawerState(
            initialValue = if (isDrawerOpen) DrawerValue.Open else DrawerValue.Closed,
            confirmStateChange = { newValue ->
                val isOpen = newValue == DrawerValue.Open
                onDrawerOpenChange(isOpen)
                true
            }
        )

        LaunchedEffect(isDrawerOpen) {
            if (isDrawerOpen) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }

        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = shouldShowNavigation,
            drawerContent = {
                MobileDrawerContent(
                    activeTab = activeTab,
                    isDarkTheme = isDarkTheme,
                    onTabSelected = onTabSelected,
                    onThemeToggle = onThemeToggle,
                    onCloseDrawer = {
                        scope.launch {
                            drawerState.close()
                            onDrawerOpenChange(false)
                        }
                    }
                )
            }
        ) {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                bottomBar = {
                    if (shouldShowNavigation) {
                        MobileBottomBar(
                            activeTab = activeTab,
                            onTabSelected = onTabSelected
                        )
                    }
                },
                topBar = {
                    if (shouldShowNavigation) {
                        MobileTopBar(
                            isThereUnreadNotifications = true,
                            onAvatarClick = { scope.launch { drawerState.open() } },
                            onNotificationsClick = {},
                            modifier = Modifier
                                .windowInsetsPadding(
                                    WindowInsets.safeDrawing
                                        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                                )
                                .padding(horizontal = 16.dp)
                        )
                    }
                },
                contentWindowInsets = if (shouldShowNavigation) WindowInsets.systemBars else WindowInsets(0, 0, 0, 0),
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .background(WhiskrTheme.colors.background)
                        .padding(innerPadding)
                ) {
                    content()
                }
            }
        }
    }
}