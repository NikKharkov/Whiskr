package org.example.whiskr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun AuthButtonContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    icon: Painter,
    iconTint: Color = Color.Unspecified,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(vertical = 16.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, WhiskrTheme.colors.outline),
        colors = ButtonDefaults.buttonColors(containerColor = WhiskrTheme.colors.background),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )

            Text(
                text = text,
                style = WhiskrTheme.typography.button,
                color = WhiskrTheme.colors.onBackground,
            )
        }
    }
}
