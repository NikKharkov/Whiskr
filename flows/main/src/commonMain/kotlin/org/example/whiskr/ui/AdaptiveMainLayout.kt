package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
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
import domain.UserState
import kotlinx.coroutines.launch
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun AdaptiveMainLayout(
    modifier: Modifier = Modifier,
    isTablet: Boolean,
    activeTab: MainFlowComponent.Tab,
    onTabSelected: (MainFlowComponent.Tab) -> Unit,
    userState: UserState,
    isDarkTheme: Boolean,
    onThemeToggle: (Offset) -> Unit,
    isDrawerOpen: Boolean,
    onDrawerOpenChange: (Boolean) -> Unit,
    shouldShowNavigation: Boolean,
    content: @Composable () -> Unit
) {
    if (isTablet) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(WhiskrTheme.colors.background)
        ) {
            TabletSidebar(
                activeTab = activeTab,
                onTabSelected = onTabSelected,
                userState = userState,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight()
                    .padding(vertical = 24.dp)
            )

            VerticalDivider(color = WhiskrTheme.colors.surface)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
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
            gesturesEnabled = true,
            drawerContent = {
                MobileDrawerContent(
                    userState = userState,
                    activeTab = activeTab,
                    isDarkTheme = isDarkTheme,
                    onTabSelected = onTabSelected,
                    onThemeToggle = onThemeToggle,
                    onCloseDrawer = {
                        onDrawerOpenChange(false)
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
                            avatarUrl = userState.profile?.avatarUrl,
                            isThereUnreadNotifications = true,
                            onAvatarClick = { scope.launch { drawerState.open() } },
                            onNotificationsClick = {},
                            modifier = Modifier.safeContentPadding()
                        )
                    }
                }
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