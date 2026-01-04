package org.example.whiskr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.UserState
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun SidebarProfileItem(
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
            AvatarPlaceholder(
                modifier = modifier,
                avatarUrl = userState.profile?.avatarUrl
            )

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