package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.user.FakeUserRegistrationComponent
import component.user.UserRegistrationComponent
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.layouts.AdaptiveLayoutWithDialog
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.components.HandleVisualTransformation
import ui.components.ProfilePhotoInput
import whiskr.features.registration.generated.resources.Res
import whiskr.features.registration.generated.resources.create_account
import whiskr.features.registration.generated.resources.name
import whiskr.features.registration.generated.resources.next
import whiskr.features.registration.generated.resources.registration_caption
import whiskr.features.registration.generated.resources.registration_title
import whiskr.features.registration.generated.resources.step1
import whiskr.features.registration.generated.resources.username

@Composable
fun UserRegistrationScreen(
    userRegistrationComponent: UserRegistrationComponent
) {
    val model by userRegistrationComponent.model.subscribeAsState()
    val focusManager = LocalFocusManager.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp

        AdaptiveLayoutWithDialog(
            isTablet = isTablet,
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isTablet) {
                        Text(
                            text = stringResource(Res.string.create_account),
                            style = WhiskrTheme.typography.h3,
                            color = WhiskrTheme.colors.secondary
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.step1),
                            style = WhiskrTheme.typography.caption,
                            color = WhiskrTheme.colors.onBackground
                        )
                    }
                }
            }
        ) {
            if (!isTablet) {
                Text(
                    text = stringResource(Res.string.registration_title),
                    style = WhiskrTheme.typography.h1,
                    color = WhiskrTheme.colors.onBackground
                )

                Text(
                    text = stringResource(Res.string.registration_caption),
                    style = WhiskrTheme.typography.body,
                    color = WhiskrTheme.colors.secondary
                )
            }

            ProfilePhotoInput(
                avatarBytes = model.avatarBytes,
                onAvatarSelected = userRegistrationComponent::onAvatarSelected
            )

            Text(
                text = stringResource(Res.string.name),
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            WhiskrTextField(
                value = model.name,
                onValueChange = userRegistrationComponent::onNameChanged,
                hint = "johndoe",
                enabled = !model.isLoading,
                keyboardType = KeyboardType.Text,
                onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Text(
                text = stringResource(Res.string.username),
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            WhiskrTextField(
                value = model.username,
                onValueChange = userRegistrationComponent::onUsernameChanged,
                hint = "@johndoe",
                enabled = !model.isLoading,
                keyboardType = KeyboardType.Ascii,
                errorMessage = model.usernameError,
                visualTransformation = HandleVisualTransformation(),
                imeAction = ImeAction.Done,
                onImeAction = {
                    focusManager.clearFocus()
                    if (model.isSubmitEnabled) {
                        userRegistrationComponent.onNextClicked()
                    }
                }
            )

            Spacer(if (isTablet) Modifier.height(16.dp) else Modifier.weight(1f))

            WhiskrButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.next),
                onClick = userRegistrationComponent::onNextClicked,
                enabled = model.isSubmitEnabled,
                contentColor = Color.White,
                isLoading = model.isLoading
            )
        }
    }
}


@Preview(name = "Light Mode", showBackground = true)
@Composable
private fun UserRegistrationScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        UserRegistrationScreen(
            userRegistrationComponent = FakeUserRegistrationComponent()
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
private fun UserRegistrationScreenDarkThemePreview() {
    WhiskrTheme(isDarkTheme = true) {
        UserRegistrationScreen(
            userRegistrationComponent = FakeUserRegistrationComponent(
                initialModel = UserRegistrationComponent.Model(
                    name = "Dark User",
                    username = "dark_knight",
                    isSubmitEnabled = true,
                    isLoading = false
                )
            )
        )
    }
}

@Preview(name = "Tablet", widthDp = 891, showBackground = true)
@Composable
private fun UserRegistrationScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = false) {
        UserRegistrationScreen(
            userRegistrationComponent = FakeUserRegistrationComponent()
        )
    }
}