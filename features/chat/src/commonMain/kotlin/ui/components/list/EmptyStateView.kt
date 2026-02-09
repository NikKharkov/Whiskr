package ui.components.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.empty_chats_subtitle
import whiskr.features.chat.generated.resources.empty_chats_title
import whiskr.features.chat.generated.resources.ic_messages

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_messages),
            contentDescription = null,
            tint = WhiskrTheme.colors.secondary.copy(alpha = 0.5f),
            modifier = Modifier.size(80.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.empty_chats_title),
            style = WhiskrTheme.typography.h3,
            color = WhiskrTheme.colors.secondary
        )

        Text(
            text = stringResource(Res.string.empty_chats_subtitle),
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.secondary
        )
    }
}