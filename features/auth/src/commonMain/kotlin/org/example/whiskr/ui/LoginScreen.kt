package org.example.whiskr.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.login.FakeLoginComponent
import org.example.whiskr.component.login.LoginComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.layouts.AdaptiveLayoutWithDialog
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import whiskr.features.auth.generated.resources.Res
import whiskr.features.auth.generated.resources.email_input_subtitle
import whiskr.features.auth.generated.resources.email_input_title
import whiskr.features.auth.generated.resources.ic_close
import whiskr.features.auth.generated.resources.next
import whiskr.features.auth.generated.resources.whisp

@Composable
fun LoginScreen(loginComponent: LoginComponent) {
    val model by loginComponent.model.subscribeAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp

        AdaptiveLayoutWithDialog(
            isTablet = isTablet,
            topBar = {
                SimpleTopBar(
                    icon = painterResource(Res.drawable.ic_close),
                    onIconClick = loginComponent::onBackClicked,
                    title = {
                        Icon(
                            painter = painterResource(Res.drawable.whisp),
                            contentDescription = "Whisp",
                            tint = WhiskrTheme.colors.primary,
                            modifier = Modifier.size(24.dp),
                        )
                    },
                )
            },
        ) {
            Text(
                text = stringResource(Res.string.email_input_title),
                style = WhiskrTheme.typography.h1,
                textAlign = if (isTablet) TextAlign.Center else TextAlign.Start,
                color = WhiskrTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = stringResource(Res.string.email_input_subtitle),
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.secondary,
                textAlign = if (isTablet) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )

            WhiskrTextField(
                value = model.email,
                onValueChange = loginComponent::onEmailChanged,
                hint = "example@gmail.com",
                errorMessage = model.errorMessage,
                enabled = !model.isLoading,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = {
                    if (model.isNextButtonEnabled && !model.isLoading) {
                        loginComponent.onNextClicked()
                    }
                },
            )

            if (!isTablet) Spacer(Modifier.weight(1f))

            WhiskrButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.next),
                onClick = loginComponent::onNextClicked,
                enabled = model.isNextButtonEnabled,
                isLoading = model.isLoading,
                contentColor = Color.White,
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun LoginScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        LoginScreen(
            loginComponent = FakeLoginComponent(),
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
private fun LoginScreenDarkThemePreview() {
    WhiskrTheme(isDarkTheme = true) {
        LoginScreen(
            loginComponent = FakeLoginComponent(),
        )
    }
}

@Preview(name = "Tablet", widthDp = 891, showBackground = true)
@Composable
private fun LoginScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = false) {
        LoginScreen(
            loginComponent = FakeLoginComponent(),
        )
    }
}
