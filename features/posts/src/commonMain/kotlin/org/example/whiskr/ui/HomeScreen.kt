package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.home.HomeComponent
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.card.PostCard
import org.example.whiskr.ui.components.CreatePostLayout
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isTablet) {
                item {
                    CreatePostLayout(
                        component = component.createPostComponent,
                        userAvatar = user?.profile?.avatarUrl
                    )

                    HorizontalDivider(thickness = 1.dp, color = WhiskrTheme.colors.outline)
                }
            }

            items(model.items) { post ->
                PostCard(
                    post = post,
                    onPostClick = { /* TODO */ },
                    onProfileClick = { component.onProfileClick(post.author.id) },
                    onLikeClick = { component.onLikeClick(post.id) },
                    onCommentClick = { /* TODO */ },
                    onRepostClick = { /* TODO */ },
                    onShareClick = { /* TODO */ }
                )

                HorizontalDivider(thickness = 1.dp, color = WhiskrTheme.colors.outline)
            }
        }
    }
}