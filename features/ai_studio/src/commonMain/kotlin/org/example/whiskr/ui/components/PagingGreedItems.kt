package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun <T> LazyListScope.paginatedGridItems(
    items: List<T>,
    columns: Int,
    contentPadding: Dp = 8.dp,
    isLoadingMore: Boolean = false,
    onLoadMore: () -> Unit,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable BoxScope.(item: T) -> Unit
) {
    val chunks = items.chunked(columns)

    itemsIndexed(
        items = chunks,
        key = key?.let { keySelector ->
            { _, rowItems -> rowItems.first().let(keySelector) }
        }
    ) { index, rowItems ->
        if (index >= chunks.lastIndex - 2 && !isLoadingMore) {
            LaunchedEffect(Unit) { onLoadMore() }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = contentPadding),
            horizontalArrangement = Arrangement.spacedBy(contentPadding)
        ) {
            rowItems.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    itemContent(item)
                }
            }

            val emptySlots = columns - rowItems.size
            if (emptySlots > 0) {
                repeat(emptySlots) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    if (isLoadingMore) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }
    }
}