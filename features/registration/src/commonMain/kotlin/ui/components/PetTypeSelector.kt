package ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.PetType
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import util.iconRes
import util.labelRes

@Composable
fun PetTypeSelector(
    modifier: Modifier = Modifier,
    selectedType: PetType?,
    onTypeSelected: (PetType) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(PetType.entries) { type ->
            PetTypeChip(
                type = type,
                isSelected = type == selectedType,
                onClick = { onTypeSelected(type) }
            )
        }
    }
}

@Composable
private fun PetTypeChip(
    type: PetType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        color = if (isSelected) WhiskrTheme.colors.primary else WhiskrTheme.colors.surface,
        border = BorderStroke(1.dp, WhiskrTheme.colors.outline)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(type.iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(type.labelRes),
                style = WhiskrTheme.typography.body,
                color = if (isSelected) Color.White else WhiskrTheme.colors.onBackground
            )
        }
    }
}