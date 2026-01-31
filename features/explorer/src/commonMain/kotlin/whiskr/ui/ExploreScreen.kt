package org.example.whiskr.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.explore.ExploreComponent
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.ExploreSearchBar
import org.example.whiskr.ui.components.NewsCard

@Composable
fun ExploreScreen(
    component: ExploreComponent
) {
    val model by component.model.subscribeAsState()

    Scaffold(
        topBar = {
            ExploreSearchBar(
                query = model.query,
                onQueryChange = component::onQueryChanged,
                onSearch = { component.onSearch(model.query) },
                isSearching = model.isSearching,
                onBackClick = component::onBackClick
            )
        },
        containerColor = WhiskrTheme.colors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        PostListContent(
            component = component.postsComponent,
            modifier = Modifier.padding(innerPadding),
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
    }
}