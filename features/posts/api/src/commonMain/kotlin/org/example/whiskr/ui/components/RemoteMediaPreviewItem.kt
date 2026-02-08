package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import org.example.whiskr.components.IconDelete
import org.example.whiskr.util.toCloudStorageUrl

@Composable
fun RemoteMediaPreviewItem(
    url: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = url.toCloudStorageUrl(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        IconDelete(
            onRemove = onRemove,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}