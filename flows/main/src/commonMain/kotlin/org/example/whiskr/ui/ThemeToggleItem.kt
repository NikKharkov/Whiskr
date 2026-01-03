package org.example.whiskr.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.flows.main.generated.resources.Res
import whiskr.flows.main.generated.resources.dark_mode
import whiskr.flows.main.generated.resources.ic_dark_mode
import whiskr.flows.main.generated.resources.ic_light_mode
import whiskr.flows.main.generated.resources.light_mode


@Composable
fun ThemeToggleItem(
    isDarkTheme: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .customClickable { onToggle(!isDarkTheme) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            AnimatedContent(targetState = isDarkTheme) { isDark ->
                Icon(
                    painter = painterResource(if (isDark) Res.drawable.ic_dark_mode else Res.drawable.ic_light_mode),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = WhiskrTheme.colors.onBackground
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(if (isDarkTheme) Res.string.dark_mode else Res.string.light_mode),
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.onBackground
            )
        }

        Switch(
            checked = isDarkTheme,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = WhiskrTheme.colors.primary,
                checkedTrackColor = WhiskrTheme.colors.primary.copy(alpha = 0.5f),
                uncheckedThumbColor = WhiskrTheme.colors.secondary,
                uncheckedTrackColor = WhiskrTheme.colors.outline,
                uncheckedBorderColor = WhiskrTheme.colors.outline
            )
        )
    }
}