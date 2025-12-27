package org.example.whiskr.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun SimpleTopBar(
    icon: Painter,
    onIconClick: () -> Unit,
    title: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Icon(
            painter = icon,
            contentDescription = null,
            tint = WhiskrTheme.colors.onBackground,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .clickable(onClick = onIconClick)
        )


        title()

        Spacer(modifier = Modifier.size(24.dp))
    }
}