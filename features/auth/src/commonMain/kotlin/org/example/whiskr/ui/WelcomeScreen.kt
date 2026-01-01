package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import org.example.whiskr.component.welcome.FakeWelcomeComponent
import org.example.whiskr.component.welcome.WelcomeComponent
import org.example.whiskr.layouts.AdaptiveSplitLayout
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.WelcomeActionsContent
import org.example.whiskr.ui.components.WelcomeHeroContent
import org.example.whiskr.util.WEB_CLIENT_ID
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun WelcomeScreen(component: WelcomeComponent) {
    BoxWithConstraints(
        modifier =
            Modifier
                .fillMaxSize()
                .background(WhiskrTheme.colors.background)
                .safeContentPadding(),
    ) {
        val isTablet = maxWidth > 600.dp

        AdaptiveSplitLayout(
            isTablet = isTablet,
            topContent = {
                WelcomeHeroContent(isTablet = isTablet)
            },
            bottomContent = {
                WelcomeActionsContent(
                    isTablet = isTablet,
                    handleAuth = component::onAuthResult,
                    onLoginClick = component::onLoginClicked,
                )
            },
        )
    }

    DisposableEffect(Unit) {
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(serverId = WEB_CLIENT_ID),
        )
        onDispose {}
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        WelcomeScreen(
            component = FakeWelcomeComponent(),
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
private fun WelcomeScreenDarkThemePreview() {
    WhiskrTheme(isDarkTheme = true) {
        WelcomeScreen(
            component = FakeWelcomeComponent(),
        )
    }
}

@Preview(name = "Tablet", widthDp = 891, showBackground = true)
@Composable
private fun WelcomeScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = false) {
        WelcomeScreen(
            component = FakeWelcomeComponent(),
        )
    }
}
