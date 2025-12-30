package ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.PetGender
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun PetGenderSelector(
    modifier: Modifier = Modifier,
    selectedGender: PetGender?,
    onGenderSelected: (PetGender) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PetGender.entries.forEach { gender ->
            PetGenderChip(
                modifier = Modifier.weight(1f),
                gender = gender,
                isSelected = gender == selectedGender,
                onClick = { onGenderSelected(gender) }
            )
        }
    }
}

@Composable
private fun PetGenderChip(
    modifier: Modifier = Modifier,
    gender: PetGender,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        color = if (isSelected) WhiskrTheme.colors.primary else WhiskrTheme.colors.surface,
        border = BorderStroke(1.dp, WhiskrTheme.colors.outline),
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = gender.name.lowercase().replaceFirstChar { it.uppercase() },
                style = WhiskrTheme.typography.body,
                color = if (isSelected) Color.White else WhiskrTheme.colors.onBackground
            )
        }
    }
}