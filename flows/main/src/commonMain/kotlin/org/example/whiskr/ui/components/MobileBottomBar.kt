package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MobileBottomBar(
    activeTab: MainFlowComponent.Tab?,
    onTabSelected: (MainFlowComponent.Tab) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = WhiskrTheme.colors.outline
        )

        NavigationBar(
            containerColor = WhiskrTheme.colors.background,
            tonalElevation = 8.dp
        ) {
            MainFlowComponent.Tab.entries.filter { it.showInBottomBar }.forEach { tab ->
                val isSelected = tab == activeTab

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            painter = painterResource(tab.icon),
                            contentDescription = stringResource(tab.label),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = WhiskrTheme.colors.primary,
                        unselectedIconColor = WhiskrTheme.colors.secondary,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}