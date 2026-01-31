package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import whiskr.features.posts.api.generated.resources.Res
import whiskr.features.posts.api.generated.resources.ic_repost
import whiskr.features.posts.api.generated.resources.user_reposted

@Composable
fun RepostLabel(reposterName: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_repost),
            contentDescription = null,
            tint = WhiskrTheme.colors.secondary,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = stringResource(Res.string.user_reposted, reposterName),
            style = WhiskrTheme.typography.caption,
            color = WhiskrTheme.colors.secondary
        )
    }
}