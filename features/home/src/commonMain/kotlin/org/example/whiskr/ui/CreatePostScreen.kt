package org.example.whiskr.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.CreatePostLayout
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import whiskr.features.home.generated.resources.Res
import whiskr.features.home.generated.resources.cancel

@Composable
fun CreatePostScreen(component: CreatePostComponent) {
    val user = LocalUser.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiskrTheme.colors.background,
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            Text(
                text = stringResource(Res.string.cancel),
                style = WhiskrTheme.typography.body.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .customClickable(onClick = component::onBackClick)
                    .padding(horizontal = 16.dp)
            )
        }
    ) { innerPadding ->
        CreatePostLayout(
            component = component,
            userAvatar = user?.profile?.avatarUrl,
            modifier = Modifier.padding(innerPadding)
        )
    }
}