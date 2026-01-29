package org.example.whiskr.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.whiskr.component.HashtagsComponent
import org.example.whiskr.component.hashtags.FakeHashtagsComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import whiskr.features.posts.impl.generated.resources.Res
import whiskr.features.posts.impl.generated.resources.ic_arrow_back

@Composable
fun HashtagFeedScreen(
    component: HashtagsComponent
) {
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
        PostListContent(
            component = component.postsComponent,
            modifier = Modifier.padding(innerPadding)
        )
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
            component = FakeHashtagsComponent()
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