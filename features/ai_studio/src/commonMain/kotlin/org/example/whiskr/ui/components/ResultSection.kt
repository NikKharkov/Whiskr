package org.example.whiskr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.example.whiskr.data.AiGalleryItemDto
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.util.toCloudStorageUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.ai_studio.generated.resources.Res
import whiskr.features.ai_studio.generated.resources.generating_placeholder
import whiskr.features.ai_studio.generated.resources.ic_download
import whiskr.features.ai_studio.generated.resources.ic_forward
import whiskr.features.ai_studio.generated.resources.ic_share

@Composable
fun ResultSection(
    isGenerating: Boolean,
    error: String?,
    latestItem: AiGalleryItemDto?,
    onDownload: (String) -> Unit,
    onShare: (String) -> Unit,
    onPost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(WhiskrTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            if (isGenerating) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = WhiskrTheme.colors.primary)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(Res.string.generating_placeholder),
                        style = WhiskrTheme.typography.caption,
                        color = WhiskrTheme.colors.onBackground
                    )
                }
            } else if (latestItem != null) {
                AsyncImage(
                    model = latestItem.imageUrl.toCloudStorageUrl(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (error != null) {
                Text(
                    text = error,
                    color = WhiskrTheme.colors.error,
                    style = WhiskrTheme.typography.body
                )
            }
        }

        if (latestItem != null && !isGenerating) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_download),
                    contentDescription = null,
                    tint = WhiskrTheme.colors.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .customClickable(onClick = { onDownload(latestItem.imageUrl.toCloudStorageUrl()) })
                )

                Icon(
                    painter = painterResource(Res.drawable.ic_forward),
                    contentDescription = null,
                    tint = WhiskrTheme.colors.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .customClickable(onClick = { onPost(latestItem.imageUrl) })
                )

                Icon(
                    painter = painterResource(Res.drawable.ic_share),
                    contentDescription = null,
                    tint = WhiskrTheme.colors.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .customClickable(onClick = { onShare(latestItem.imageUrl.toCloudStorageUrl()) })
                )
            }
        }
    }
}