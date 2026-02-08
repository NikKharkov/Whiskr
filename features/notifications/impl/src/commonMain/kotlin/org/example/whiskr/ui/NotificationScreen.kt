package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.NotificationComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.NotificationCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.notifications.impl.generated.resources.Res
import whiskr.features.notifications.impl.generated.resources.ic_arrow_back
import whiskr.features.notifications.impl.generated.resources.notifications_empty
import whiskr.features.notifications.impl.generated.resources.notifications_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    component: NotificationComponent
) {
    val model by component.model.subscribeAsState()
    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullToRefreshState()
    val isTablet = LocalIsTablet.current

    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleItemIndex >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !model.isLoadingMore) {
            component.onLoadMore()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiskrTheme.colors.background,
        topBar = {
            if (!isTablet) {
                SimpleTopBar(
                    icon = painterResource(Res.drawable.ic_arrow_back),
                    onIconClick = component::onBackClicked,
                    title = {
                        Text(
                            text = stringResource(Res.string.notifications_title),
                            style = WhiskrTheme.typography.h3,
                            color = WhiskrTheme.colors.onBackground,
                        )
                    },
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = model.isRefreshing,
            onRefresh = component::onRefresh,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullRefreshState,
                    isRefreshing = model.isRefreshing,
                    containerColor = WhiskrTheme.colors.surface,
                    color = WhiskrTheme.colors.primary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            if (model.items.isEmpty() && !model.isRefreshing) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.notifications_empty),
                        style = WhiskrTheme.typography.body,
                        color = WhiskrTheme.colors.onBackground.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = model.items,
                        key = { it.id }
                    ) { item ->
                        NotificationCard(
                            item = item,
                            onClick = { component.onNotificationClicked(item) }
                        )
                    }

                    if (model.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = WhiskrTheme.colors.primary,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}