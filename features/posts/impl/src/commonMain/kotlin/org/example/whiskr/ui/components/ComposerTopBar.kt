package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.stringResource
import whiskr.features.posts.impl.generated.resources.Res
import whiskr.features.posts.impl.generated.resources.cancel
import whiskr.features.posts.impl.generated.resources.post

@Composable
fun ComposerTopBar(
    onCancel: () -> Unit,
    onSend: () -> Unit,
    isSending: Boolean,
    isSendEnabled: Boolean,
    actionLabel: String = stringResource(Res.string.post),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.cancel),
            style = WhiskrTheme.typography.body.copy(fontWeight = FontWeight.Medium),
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.customClickable(onClick = onCancel)
        )

        WhiskrButton(
            onClick = onSend,
            enabled = isSendEnabled,
            text = actionLabel,
            isLoading = isSending,
            shape = CircleShape,
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 8.dp),
            modifier = Modifier.height(50.dp),
            contentColor = Color.White
        )
    }
}