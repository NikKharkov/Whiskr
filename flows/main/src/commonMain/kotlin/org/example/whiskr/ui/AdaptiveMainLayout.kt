package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import domain.UserState
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
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                MobileBottomBar(
                    activeTab = activeTab,
                    onTabSelected = onTabSelected
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}