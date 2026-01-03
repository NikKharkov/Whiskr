package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import domain.UserState
import org.example.whiskr.component.MainFlowComponent
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.flows.main.generated.resources.Res
import whiskr.flows.main.generated.resources.ic_avatar_placeholder
import whiskr.flows.main.generated.resources.post

@Composable
fun TabletSidebar(
    modifier: Modifier = Modifier,
    userState: UserState,
    activeTab: MainFlowComponent.Tab,
    isDarkTheme: Boolean,
    onThemeToggle: (Offset) -> Unit,
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
            onClick = { TODO("Go to POST Screen") }
        )

        SidebarProfileItem(
            userState = userState,
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )
    }
}

@Composable
private fun SidebarProfileItem(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeToggle: (Offset) -> Unit,
    userState: UserState
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (userState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = WhiskrTheme.colors.primary
            )
        } else {
            Box(
                modifier = modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(WhiskrTheme.colors.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_avatar_placeholder),
                    contentDescription = null,
                    tint = WhiskrTheme.colors.onBackground,
                    modifier = Modifier.size(24.dp)
                )

                AsyncImage(
                    model = userState.profile?.avatarUrl?.replace("localhost", "10.0.2.2"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userState.profile?.displayName ?: "User",
                    style = WhiskrTheme.typography.body,
                    fontWeight = FontWeight.Bold,
                    color = WhiskrTheme.colors.onBackground,
                    maxLines = 1
                )
                Text(
                    text = userState.profile?.handle ?: "@user",
                    style = WhiskrTheme.typography.body,
                    color = WhiskrTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }

            ThemeToggleIconButton(
                isDarkTheme = isDarkTheme,
                onClick = { centerOffset ->
                    onThemeToggle(centerOffset)
                }
            )
        }
    }
}