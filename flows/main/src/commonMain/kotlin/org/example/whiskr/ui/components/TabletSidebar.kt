package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.flows.main.generated.resources.Res
import whiskr.flows.main.generated.resources.post

@Composable
fun TabletSidebar(
    modifier: Modifier = Modifier,
    activeTab: MainFlowComponent.Tab?,
    isDarkTheme: Boolean,
    onThemeToggle: (Offset) -> Unit,
    onPostClick: () -> Unit,
    onTabSelected: (MainFlowComponent.Tab) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MainFlowComponent.Tab.entries.forEach { tab ->
            val isSelected = tab == activeTab

            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(tab.label),
                        style = WhiskrTheme.typography.h3.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
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
                onClick = { onTabSelected(tab) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = WhiskrTheme.colors.primary.copy(alpha = 0.25f),
                    selectedIconColor = WhiskrTheme.colors.primary,
                    selectedTextColor = WhiskrTheme.colors.primary,
                    unselectedIconColor = WhiskrTheme.colors.onBackground,
                    unselectedTextColor = WhiskrTheme.colors.onBackground
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        WhiskrButton(
            text = stringResource(Res.string.post),
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth(),
            onClick = onPostClick
        )

        SidebarProfileItem(
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )
    }
}