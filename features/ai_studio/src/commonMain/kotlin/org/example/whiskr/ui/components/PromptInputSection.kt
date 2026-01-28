package org.example.whiskr.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.ai_studio.generated.resources.Res
import whiskr.features.ai_studio.generated.resources.delete
import whiskr.features.ai_studio.generated.resources.describe
import whiskr.features.ai_studio.generated.resources.ic_close
import whiskr.features.ai_studio.generated.resources.ic_gallery
import whiskr.features.ai_studio.generated.resources.ic_send
import whiskr.features.ai_studio.generated.resources.prompt_placeholder
import whiskr.features.ai_studio.generated.resources.send
import whiskr.features.ai_studio.generated.resources.upload

@Composable
fun PromptInputSection(
    prompt: String,
    onPromptChange: (String) -> Unit,
    isGenerating: Boolean,
    sourceImageBytes: ByteArray?,
    onPickImage: () -> Unit,
    onRemoveImage: () -> Unit,
    onGenerate: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.describe),
            style = WhiskrTheme.typography.h3,
            color = WhiskrTheme.colors.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WhiskrTextField(
                modifier = Modifier.weight(1f),
                value = prompt,
                onValueChange = onPromptChange,
                hint = stringResource(Res.string.prompt_placeholder),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_gallery),
                        contentDescription = stringResource(Res.string.upload),
                        tint = if (sourceImageBytes != null) WhiskrTheme.colors.primary else WhiskrTheme.colors.secondary,
                        modifier = Modifier
                            .size(24.dp)
                            .customClickable(onClick = onPickImage)
                    )
                },
                onImeAction = {
                    if (!isGenerating && (prompt.isNotBlank() || sourceImageBytes != null)) {
                        onGenerate()
                    }
                }
            )

            val isSendEnabled = !isGenerating && (prompt.isNotBlank() || sourceImageBytes != null)

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSendEnabled) WhiskrTheme.colors.primary
                        else WhiskrTheme.colors.secondary.copy(alpha = 0.3f)
                    )
                    .customClickable(
                        onClick = onGenerate,
                        enabled = isSendEnabled
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.ic_send),
                        contentDescription = stringResource(Res.string.send),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        AnimatedVisibility(visible = sourceImageBytes != null) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, WhiskrTheme.colors.outline, RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = sourceImageBytes,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(24.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .customClickable(onClick = onRemoveImage),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.delete),
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}