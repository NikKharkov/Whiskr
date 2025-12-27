package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import dev.gitlive.firebase.auth.FirebaseUser
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.auth.generated.resources.Res
import whiskr.features.auth.generated.resources.already_have_account
import whiskr.features.auth.generated.resources.continue_with_apple
import whiskr.features.auth.generated.resources.continue_with_google
import whiskr.features.auth.generated.resources.create_account
import whiskr.features.auth.generated.resources.ic_apple
import whiskr.features.auth.generated.resources.ic_google
import whiskr.features.auth.generated.resources.log_in
import whiskr.features.auth.generated.resources.or
import whiskr.features.auth.generated.resources.sign_in
import whiskr.features.auth.generated.resources.welcome_text

@Composable
fun WelcomeActionsContent(
    modifier: Modifier = Modifier,
    isTablet: Boolean,
    handleAuth: (Result<FirebaseUser?>) -> Unit,
    onLoginClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.welcome_text),
            style = WhiskrTheme.typography.h1,
            color = WhiskrTheme.colors.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        GoogleButtonUiContainerFirebase(
            onResult = handleAuth,
            linkAccount = false,
        ) {
            AuthButtonContent(
                modifier = Modifier.fillMaxWidth(),
                onClick = { this.onClick() },
                icon = painterResource(Res.drawable.ic_google),
                text = stringResource(Res.string.continue_with_google),
            )
        }

        AppleButtonUiContainer(
            onResult = handleAuth,
            linkAccount = false,
        ) {
            AuthButtonContent(
                modifier = Modifier.fillMaxWidth(),
                onClick = { this.onClick() },
                icon = painterResource(Res.drawable.ic_apple),
                iconTint = WhiskrTheme.colors.onBackground,
                text = stringResource(Res.string.continue_with_apple),
            )
        }

        OrDivider()

        WhiskrButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.create_account),
            onClick = onLoginClick,
            contentColor = Color.White,
        )

        if (isTablet) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(Res.string.already_have_account),
                    style = WhiskrTheme.typography.body.copy(fontWeight = FontWeight.Bold),
                    color = WhiskrTheme.colors.onBackground,
                )

                WhiskrButton(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = WhiskrTheme.colors.background,
                    contentColor = WhiskrTheme.colors.primary,
                    borderColor = WhiskrTheme.colors.outline,
                    text = stringResource(Res.string.sign_in),
                    onClick = onLoginClick,
                )
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.customClickable(onClick = onLoginClick),
            ) {
                Text(
                    text = stringResource(Res.string.already_have_account) + " ",
                    style = WhiskrTheme.typography.caption,
                    color = WhiskrTheme.colors.secondary,
                )

                Text(
                    text = stringResource(Res.string.log_in),
                    style = WhiskrTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                    color = WhiskrTheme.colors.primary,
                )
            }
        }
    }
}

@Composable
private fun OrDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = WhiskrTheme.colors.outline,
        )

        Text(
            text = stringResource(Res.string.or),
            style = WhiskrTheme.typography.caption,
            color = WhiskrTheme.colors.outline,
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = WhiskrTheme.colors.outline,
        )
    }
}
