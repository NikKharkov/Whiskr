package org.example.whiskr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.whiskr.component.verification.FakeVerificationComponent
import org.example.whiskr.component.verification.VerificationComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.layouts.AdaptiveLayoutWithDialog
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.WhiskrOtpInput
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.auth.generated.resources.Res
import whiskr.features.auth.generated.resources.ic_arrow_back
import whiskr.features.auth.generated.resources.next
import whiskr.features.auth.generated.resources.otp_code_sent
import whiskr.features.auth.generated.resources.otp_resend_now
import whiskr.features.auth.generated.resources.otp_resend_timer
import whiskr.features.auth.generated.resources.otp_sent_description
import whiskr.features.auth.generated.resources.otp_toolbar_title

@Composable
fun VerifyScreen(component: VerificationComponent) {
    val model by component.model.subscribeAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp

        AdaptiveLayoutWithDialog(
            isTablet = isTablet,
            topBar = {
                SimpleTopBar(
                    icon = painterResource(Res.drawable.ic_arrow_back),
                    onIconClick = component::onBackClicked,
                    title = {
                        Text(
                            text = stringResource(Res.string.otp_toolbar_title),
                            style = WhiskrTheme.typography.body.copy(fontWeight = FontWeight.Bold),
                            color = WhiskrTheme.colors.onBackground,
                        )
                    },
                )
            },
        ) {
            Text(
                text = stringResource(Res.string.otp_code_sent),
                style = WhiskrTheme.typography.h1,
                textAlign = if (isTablet) TextAlign.Center else TextAlign.Start,
                color = WhiskrTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = stringResource(Res.string.otp_sent_description, model.email),
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.secondary,
                textAlign = if (isTablet) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )

            WhiskrOtpInput(
                code = model.code,
                onValueChange = component::onCodeChanged,
                errorMessage = model.error,
                enabled = !model.isLoading,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                if (model.isResendEnabled) {
                    Text(
                        text = stringResource(Res.string.otp_resend_now),
                        color = WhiskrTheme.colors.primary,
                        modifier = Modifier.customClickable(onClick = component::onResendClicked),
                    )
                } else {
                    val minutes = model.timerSeconds / 60
                    val seconds = model.timerSeconds % 60
                    val timeString = "$minutes:${seconds.toString().padStart(2, '0')}"

                    Text(
                        text = stringResource(Res.string.otp_resend_timer, timeString),
                        color = WhiskrTheme.colors.secondary,
                        style = WhiskrTheme.typography.caption,
                    )
                }
            }

            if (!isTablet) Spacer(Modifier.weight(1f))

            WhiskrButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.next),
                onClick = component::onVerifyClicked,
                enabled = model.isVerifyEnabled,
                contentColor = Color.White,
                isLoading = model.isLoading,
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun VerifyScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        VerifyScreen(
            component = FakeVerificationComponent(),
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
private fun VerifyScreenDarkThemePreview() {
    WhiskrTheme(isDarkTheme = true) {
        VerifyScreen(
            component = FakeVerificationComponent(),
        )
    }
}

@Preview(name = "Tablet", widthDp = 891, showBackground = true)
@Composable
private fun VerifyScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = false) {
        VerifyScreen(
            component = FakeVerificationComponent(),
        )
    }
}
