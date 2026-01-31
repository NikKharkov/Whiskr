package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.PostListComponent
import org.example.whiskr.layouts.pagingItems
import org.example.whiskr.theme.WhiskrTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListContent(
    component: PostListComponent,
    modifier: Modifier = Modifier,
    headerContent: (@Composable () -> Unit)? = null
) {
    val model by component.model.subscribeAsState()
    val state = model.listState
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        state = pullRefreshState,
        isRefreshing = state.isRefreshing,
        onRefresh = component::onRefresh,
        modifier = modifier.fillMaxSize(),
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullRefreshState,
                isRefreshing = state.isRefreshing,
                containerColor = WhiskrTheme.colors.surface,
                color = WhiskrTheme.colors.primary,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (headerContent != null) {
                item {
                    headerContent()

                    HorizontalDivider(thickness = 1.dp, color = WhiskrTheme.colors.outline)
                }
            }

            pagingItems(
                items = state.items,
                isLoadingMore = state.isLoadingMore,
                isEndOfList = state.isEndOfList,
                onLoadMore = component::onLoadMore,
                separatorContent = { _, _ ->
                    HorizontalDivider(thickness = 1.dp, color = WhiskrTheme.colors.outline)
                }
            ) { _, post ->
                PostCard(
                    post = post,
                    onMediaClick = { mediaList, index -> component.onMediaClick(mediaList, index) },
                    onProfileClick = { handle -> component.onProfileClick(handle) },
                    onLikeClick = { component.onLikeClick(post.id) },
                    onCommentClick = { component.onNavigateToDetails(post) },
                    onRepostClick = { component.onRepostClick(post) },
                    onShareClick = { component.onShareClick(post) },
                    onHashtagClick = { tag -> component.onHashtagClick(tag) },
                    onNavigateToOriginal = { originalPost -> component.onNavigateToDetails(originalPost) }
                )
            }
        }
    }
}
