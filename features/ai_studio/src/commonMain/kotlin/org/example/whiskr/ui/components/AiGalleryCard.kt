package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.example.whiskr.data.AiGalleryItemDto
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.util.toCloudStorageUrl

@Composable
fun AiGalleryCard(
    item: AiGalleryItemDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = item.imageUrl.toCloudStorageUrl(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .customClickable(onClick = onClick)
    )
}