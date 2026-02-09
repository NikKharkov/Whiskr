package ui.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.dto.ProfileResponse
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun UserSearchItem(
    profile: ProfileResponse,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AvatarPlaceholder(avatarUrl = profile.avatarUrl)

        Column {
            Text(
                text = profile.displayName ?: profile.handle,
                style = WhiskrTheme.typography.body.copy(fontWeight = FontWeight.Bold),
                color = WhiskrTheme.colors.onBackground
            )

            Text(
                text = profile.handle,
                style = WhiskrTheme.typography.caption,
                color = WhiskrTheme.colors.secondary
            )
        }
    }
}