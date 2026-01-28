package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.example.whiskr.data.AiGalleryItemDto
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.util.toCloudStorageUrl

fun LazyListScope.galleryGridItems(
    items: List<AiGalleryItemDto>,
    columns: Int = 3,
    onItemClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean
) {
    val chunks = items.chunked(columns)

    itemsIndexed(
        items = chunks,
        key = { _, rowItems -> rowItems.first().id }
    ) { index, rowItems ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rowItems.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = item.imageUrl.toCloudStorageUrl(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .customClickable { onItemClick(item.imageUrl) }
                    )
                }
            }

            val emptySlots = columns - rowItems.size
            repeat(emptySlots) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        if (index >= chunks.size - 2 && !isLoadingMore) {
            LaunchedEffect(Unit) { onLoadMore() }
        }
    }

    if (isLoadingMore) {
        item {
            Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }
    }
}