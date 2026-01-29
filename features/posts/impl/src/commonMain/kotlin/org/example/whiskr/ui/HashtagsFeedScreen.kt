package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.PagingDelegate
import org.example.whiskr.component.HashtagsComponent
import org.example.whiskr.component.hashtags.FakeHashtagsComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.layouts.pagingItems
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.PostCard
import org.jetbrains.compose.resources.painterResource
import whiskr.features.posts.impl.generated.resources.Res
import whiskr.features.posts.impl.generated.resources.ic_arrow_back

@Composable
fun HashtagFeedScreen(
    component: HashtagsComponent
) {
    val model by component.model.subscribeAsState()
    val state = model.listState

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiskrTheme.colors.background,
        topBar = {
            SimpleTopBar(
                icon = painterResource(Res.drawable.ic_arrow_back),
                onIconClick = component::onBackClick,
                title = {
                    Text(
                        text = "#${component.hashtag}",
                        style = WhiskrTheme.typography.h3,
                        color = WhiskrTheme.colors.onBackground
                    )
                },
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            pagingItems(
                items = state.items,
                isLoadingMore = state.isLoadingMore,
                isEndOfList = state.isEndOfList,
                onLoadMore = component::onLoadMore,
                separatorContent = { _, _ ->
                    HorizontalDivider(thickness = 1.dp, color = WhiskrTheme.colors.outline)
                }
            ) { index, post ->
                PostCard(
                    post = post,
                    onPostClick = { mediaIndex -> component.onMediaClick(post.media, mediaIndex) },
                    onProfileClick = { TODO() },
                    onLikeClick = { component.onLikeClick(post.id) },
                    onCommentClick = { component.onCommentsClick(post) },
                    onRepostClick = { /* TODO */ },
                    onShareClick = { component.onShareClick(post) },
                    onHashtagClick = { tag -> component.onHashtagClick(tag) }
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun HashtagsScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        HashtagFeedScreen(
            component = FakeHashtagsComponent()
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
private fun HashtagsScreenDarkThemePreview() {
    WhiskrTheme(isDarkTheme = true) {
        HashtagFeedScreen(
            component = FakeHashtagsComponent(
                initialModel = HashtagsComponent.Model(
                    listState = PagingDelegate.State(
                        items = emptyList(),
                        isLoadingMore = true,
                        isEndOfList = false
                    )
                )
            )
        )
    }
}

@Preview(name = "Tablet", widthDp = 891, showBackground = true)
@Composable
private fun HashtagsScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = false) {
        HashtagFeedScreen(
            component = FakeHashtagsComponent()
        )
    }
}