package org.example.whiskr.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.UserState
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.flows.main.generated.resources.Res
import whiskr.flows.main.generated.resources.followers

@Composable
fun MobileDrawerContent(
    userState: UserState,
    activeTab: MainFlowComponent.Tab,
    isDarkTheme: Boolean,
    onTabSelected: (MainFlowComponent.Tab) -> Unit,
    onThemeToggle: (Offset) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.75f),
        drawerContainerColor = WhiskrTheme.colors.surface,
        drawerContentColor = WhiskrTheme.colors.onBackground
    ) {
        SidebarProfileItem(
            userState = userState,
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )

        Text(
            text = stringResource(Res.string.followers, userState.profile?.followersCount ?: 0),
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.padding(12.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        MainFlowComponent.Tab.entries.filter { !it.showInBottomBar }.forEach { tab ->
            val isSelected = tab == activeTab

            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(tab.label),
                        style = WhiskrTheme.typography.body.copy(fontSize = 18.sp)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(tab.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                selected = isSelected,
                onClick = {
                    onTabSelected(tab)
                    onCloseDrawer()
                },
                modifier = Modifier.padding(horizontal = 12.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = WhiskrTheme.colors.primary.copy(alpha = 0.1f),
                    selectedTextColor = WhiskrTheme.colors.primary,
                    selectedIconColor = WhiskrTheme.colors.primary,
                    unselectedTextColor = WhiskrTheme.colors.onBackground,
                    unselectedIconColor = WhiskrTheme.colors.onBackground
                )
            )
        }
    }
}