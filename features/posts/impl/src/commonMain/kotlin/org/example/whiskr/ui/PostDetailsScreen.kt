package org.example.whiskr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.PostDetailsComponent
import org.example.whiskr.component.details.FakePostDetailsComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.ParentPost
import org.example.whiskr.util.mockPost
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.posts.impl.generated.resources.Res
import whiskr.features.posts.impl.generated.resources.ic_arrow_back
import whiskr.features.posts.impl.generated.resources.ic_comments
import whiskr.features.posts.impl.generated.resources.post
import whiskr.features.posts.impl.generated.resources.post_error
import whiskr.features.posts.impl.generated.resources.reply

@Composable
fun PostDetailsScreen(
    component: PostDetailsComponent
) {
    val model by component.model.subscribeAsState()

    Scaffold(
        containerColor = WhiskrTheme.colors.background,
        topBar = {
            SimpleTopBar(
                icon = painterResource(Res.drawable.ic_arrow_back),
                onIconClick = component::onBackClick,
                title = {
                    Text(
                        text = stringResource(Res.string.post),
                        style = WhiskrTheme.typography.h3,
                        color = WhiskrTheme.colors.onBackground
                    )
                },
                modifier = Modifier.statusBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp)
            )
        },
        floatingActionButton = {
            model.post?.let { post ->
                FloatingActionButton(
                    onClick = { component.onReplyClick(post) },
                    containerColor = WhiskrTheme.colors.primary,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_comments),
                        contentDescription = stringResource(Res.string.reply),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        PostListContent(
            component = component.repliesComponent,
            modifier = Modifier.padding(innerPadding),
            headerContent = {
                when {
                    model.isLoadingPost -> {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = WhiskrTheme.colors.primary)
                        }
                    }
                    model.isError -> {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(Res.string.post_error),
                                style = WhiskrTheme.typography.h3,
                                color = WhiskrTheme.colors.error
                            )
                        }
                    }
                    model.post != null -> {
                        val post = model.post!!
                        Column {
                            ParentPost(
                                post = post,
                                onLikeClick = { component.onLikeClick(post.id) },
                                onMediaClick = { idx -> component.onMediaClick(post.media, idx) },
                                onCommentsClick = { component.onReplyClick(post) },
                                onRepostClick = { /* ... */ },
                                onShareClick = { component.onShareClick(post) },
                                onProfileClick = { /* ... */ }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun PostDetailsScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        PostDetailsScreen(
            component = FakePostDetailsComponent()
        )

    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
private fun PostDetailsScreenDarkThemePreview() {
    WhiskrTheme(isDarkTheme = true) {
        PostDetailsScreen(
            component = FakePostDetailsComponent(
                initialModel = PostDetailsComponent.Model(
                    post = mockPost.copy(content = "Dark mode requires high contrast!"),
                    isLoadingPost = false,
                    isError = false
                )
            )
        )
    }
}

@Preview(name = "Tablet", widthDp = 891, showBackground = true)
@Composable
private fun PostDetailsScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = false) {
        PostDetailsScreen(
            component = FakePostDetailsComponent()
        )
    }
}