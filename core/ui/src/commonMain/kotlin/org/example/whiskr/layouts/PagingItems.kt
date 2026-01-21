package org.example.whiskr.layouts

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import org.example.whiskr.theme.WhiskrTheme

fun <T> LazyListScope.pagingItems(
    items: List<T>,
    isLoadingMore: Boolean,
    isEndOfList: Boolean,
    onLoadMore: () -> Unit,
    threshold: Int = 3,
    key: ((item: T) -> Any)? = null,
    separatorContent: (@Composable (index: Int, item: T) -> Unit)? = null,
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    itemsIndexed(
        items = items,
        key = key?.let { k -> { _, item -> k(item) } }
    ) { index, item ->
        if (index >= items.lastIndex - threshold && !isLoadingMore && !isEndOfList) {
            LaunchedEffect(Unit) {
                onLoadMore()
            }
        }

        itemContent(index, item)

        if (separatorContent != null && index < items.lastIndex) {
            separatorContent(index, item)
        }
    }

    if (isLoadingMore) {
        item(key = "pagination_loader") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = WhiskrTheme.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}