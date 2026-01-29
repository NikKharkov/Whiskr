package org.example.whiskr.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.example.whiskr.component.HomeComponent
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.CreatePostLayout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import whiskr.features.home.generated.resources.Res
import whiskr.features.home.generated.resources.add_post
import whiskr.features.home.generated.resources.ic_add

@Composable
fun HomeScreen(
    component: HomeComponent
) {
    val isTablet = LocalIsTablet.current
    val user = LocalUser.current

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
        },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { paddingValues ->
        PostListContent(
            component = component.postsComponent,
            modifier = Modifier.padding(paddingValues),
            headerContent = if (isTablet) {
                {
                    CreatePostLayout(
                        component = component.createPostComponent,
                        userAvatar = user?.profile?.avatarUrl,
                        modifier = Modifier.statusBarsPadding()
                    )
                }
            } else null
        )
    }
}
