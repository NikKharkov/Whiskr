package org.example.whiskr.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.whiskr.data.AiArtStyle
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StyleSelector(
    selectedStyle: AiArtStyle?,
    onStyleSelect: (AiArtStyle) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Pick a vibe",
            style = WhiskrTheme.typography.h3,
            color = WhiskrTheme.colors.onBackground
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(AiArtStyle.entries.toTypedArray()) { style ->
                StyleCard(
                    style = style,
                    isSelected = style == selectedStyle,
                    onClick = { onStyleSelect(style) }
                )
            }
        }
    }
}

@Composable
private fun StyleCard(
    style: AiArtStyle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(128.dp)
            .clip(RoundedCornerShape(16.dp))
            .customClickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) WhiskrTheme.colors.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Image(
            painter = painterResource(style.artImage),
            contentDescription = stringResource(style.displayName),
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = stringResource(style.displayName),
            style = WhiskrTheme.typography.button.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}