package org.example.whiskr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun ThemeToggleIconButton(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onClick: (Offset) -> Unit
) {
    var globalCenter by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .onGloballyPositioned { layoutCoordinates ->
                val position = layoutCoordinates.positionInWindow()
                val size = layoutCoordinates.size
                globalCenter = Offset(
                    x = position.x + (size.width / 2f),
                    y = position.y + (size.height / 2f)
                )
            }
            .customClickable(onClick = { onClick(globalCenter) }),
        contentAlignment = Alignment.Center
    ) {
        MoonToSunSwitcher(
            isMoon = isDarkTheme,
            color = WhiskrTheme.colors.onBackground
        )
    }
}