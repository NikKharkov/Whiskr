package org.example.whiskr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import org.example.whiskr.component.viewer.NewsViewerComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import whiskr.features.explorer.generated.resources.Res
import whiskr.features.explorer.generated.resources.ic_arrow_back

@Composable
fun NewsViewerScreen(
    component: NewsViewerComponent
) {
    val state = rememberWebViewState(url = component.url)
    val navigator = rememberWebViewNavigator()

    Scaffold(
        topBar = {
            Column {
                SimpleTopBar(
                    title = {
                        Text(
                            text = state.pageTitle ?: component.url,
                            style = WhiskrTheme.typography.caption,
                            color = WhiskrTheme.colors.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    icon = painterResource(Res.drawable.ic_arrow_back),
                    onIconClick = component::onBackClick
                )

                val loadingState = state.loadingState

                if (loadingState is LoadingState.Loading) {
                    LinearProgressIndicator(
                        progress = { loadingState.progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = WhiskrTheme.colors.primary,
                        trackColor = WhiskrTheme.colors.surface
                    )
                }
            }
        },
        containerColor = WhiskrTheme.colors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        WebView(
            state = state,
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}