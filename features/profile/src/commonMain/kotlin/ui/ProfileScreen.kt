package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.ProfileComponent
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.PostListContent
import ui.components.ProfileHeaderContent

@Composable
fun ProfileScreen(
    component: ProfileComponent
) {
    val model by component.model.subscribeAsState()

    Scaffold(
        containerColor = WhiskrTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PostListContent(
                component = component.postsComponent,
                headerContent = {
                    ProfileHeaderContent(
                        model = model,
                        onBackClick = component::onBackClick,
                        onEditClick = component::onEditProfileClick,
                        onFollowClick = component::onFollowClick,
                        onPetClick = component::onPetClick,
                        onAddPetClick = { TODO() },
                        onMessageClick = { TODO() }
                    )
                }
            )
        }
    }
}