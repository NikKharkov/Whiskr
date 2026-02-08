package org.example.whiskr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.explore.ExploreComponent
import org.example.whiskr.layouts.CollapsingTopBarScaffold
import org.example.whiskr.ui.components.ExploreSearchBar
import org.example.whiskr.ui.components.NewsCard

@Composable
fun ExploreScreen(
    component: ExploreComponent
) {
    val model by component.model.subscribeAsState()

    CollapsingTopBarScaffold(
        topBarContentHeight = 72.dp,
        useStatusBarPadding = true,
        topBar = { _ ->
            Box(contentAlignment = Alignment.Center) {
                ExploreSearchBar(
                    query = model.query,
                    onQueryChange = component::onQueryChanged,
                    onSearch = { component.onSearch(model.query) },
                    isSearching = model.isSearching,
                    onBackClick = component::onBackClick
                )
            }
        }
    ) { contentPadding ->
        PostListContent(
            component = component.postsComponent,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding(),
                bottom = 16.dp
            ),
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