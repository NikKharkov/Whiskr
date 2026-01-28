package org.example.whiskr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.ai_studio.generated.resources.Res
import whiskr.features.ai_studio.generated.resources.ai_studio
import whiskr.features.ai_studio.generated.resources.ic_coin

@Composable
fun AiTopBar(
    balance: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(Res.string.ai_studio),
            style = WhiskrTheme.typography.h1,
            color = WhiskrTheme.colors.onBackground
        )

        Surface(
            color = WhiskrTheme.colors.surface,
            shape = CircleShape,
            border = BorderStroke(
                1.dp,
                Brush.linearGradient(WhiskrTheme.colors.vipGradient)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = balance.toString(),
                    style = WhiskrTheme.typography.button,
                    color = WhiskrTheme.colors.onBackground
                )

                Icon(
                    painter = painterResource(Res.drawable.ic_coin),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}