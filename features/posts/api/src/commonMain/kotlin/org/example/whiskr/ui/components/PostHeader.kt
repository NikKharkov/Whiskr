package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun PostHeader(
    displayName: String,
    handle: String,
    formattedTime: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = displayName,
            style = WhiskrTheme.typography.h3,
            color = WhiskrTheme.colors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "$handle · $formattedTime",
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}