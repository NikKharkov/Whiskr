package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.home.HomeComponent
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.CreatePostLayout
import org.example.whiskr.ui.components.PostCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.add_post
import whiskr.features.posts.generated.resources.ic_add

@Composable
fun HomeScreen(
    component: HomeComponent
) {
    val model by component.model.subscribeAsState()
    val user = LocalUser.current
    val isTablet = LocalIsTablet.current

    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiskrTheme.colors.background,
        floatingActionButton = {
            if (!isTablet) {
                FloatingActionButton(
                    onClick = component::onNavigateToCreatePostScreen,
                    containerColor = WhiskrTheme.colors.primary,
                    shape = CircleShape,
                    contentColor = Color.White
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = stringResource(Res.string.add_post)
                    )
                }
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = model.isRefreshing,
            onRefresh = component::onRefresh,
            modifier = Modifier.fillMaxSize(),
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isTablet) {
                    item {
                        CreatePostLayout(
                            component = component.createPostComponent,
                            userAvatar = user?.profile?.avatarUrl,
                            modifier = Modifier.statusBarsPadding()
                        )
                        HorizontalDivider(thickness = 1.dp, color = WhiskrTheme.colors.outline)
                    }
                }

                itemsIndexed(items = model.items) { index, post ->

                    if (index >= model.items.lastIndex - 3 && !model.isLoadingMore && !model.isEndOfList) {
                        LaunchedEffect(Unit) {
                            component.onLoadMore()
                        }
                    }

                    PostCard(
                        post = post,
                        onPostClick = { mediaIndex ->
                            component.onMediaClick(post.media, mediaIndex)
                        },
                        onProfileClick = { component.onProfileClick(post.author.id) },
                        onLikeClick = { component.onLikeClick(post.id) },
                        onCommentClick = { component.onCommentsClick(post) },
                        onRepostClick = { /* TODO */ },
                        onShareClick = { /* TODO */ }
                    )

                    HorizontalDivider(thickness = 1.dp, color = WhiskrTheme.colors.outline)
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
                                color = WhiskrTheme.colors.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}