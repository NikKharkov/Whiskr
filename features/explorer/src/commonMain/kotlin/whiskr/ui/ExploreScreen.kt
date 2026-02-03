package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.explore.ExploreComponent
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.ExploreSearchBar
import org.example.whiskr.ui.components.NewsCard
import kotlin.math.roundToInt

@Composable
fun ExploreScreen(
    component: ExploreComponent
) {
    val model by component.model.subscribeAsState()
    val density = LocalDensity.current
    val toolbarHeight = 72.dp
    val toolbarHeightPx = with(density) { toolbarHeight.toPx() }

    var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx + delta
                toolbarOffsetHeightPx = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Scaffold(
        containerColor = WhiskrTheme.colors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.nestedScroll(nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PostListContent(
                component = component.postsComponent,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = toolbarHeight + 8.dp, bottom = 16.dp),
                interval = 2,
                additionalContent = { index ->
                    val newsItem = model.news.getOrNull(index % model.news.size.coerceAtLeast(1))
                    if (newsItem != null) {
                        NewsCard(
                            news = newsItem,
                            onClick = { component.onNewsClick(newsItem.link) }
                        )
                    }
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt()) }
                    .background(WhiskrTheme.colors.background)
                    .align(Alignment.TopCenter)
            ) {
                ExploreSearchBar(
                    query = model.query,
                    onQueryChange = component::onQueryChanged,
                    onSearch = { component.onSearch(model.query) },
                    isSearching = model.isSearching,
                    onBackClick = component::onBackClick
                )
            }
        }
    }
}