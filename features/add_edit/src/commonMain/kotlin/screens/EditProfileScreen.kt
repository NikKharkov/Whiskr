package screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.edit_profile.EditProfileComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.layouts.AdaptiveLayoutWithDialog
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import screens.components.HandleVisualTransformation
import screens.components.ProfilePhotoSelector
import whiskr.features.add_edit.generated.resources.Res
import whiskr.features.add_edit.generated.resources.bio_hint
import whiskr.features.add_edit.generated.resources.bio_label
import whiskr.features.add_edit.generated.resources.edit_profile_title
import whiskr.features.add_edit.generated.resources.handle_label
import whiskr.features.add_edit.generated.resources.ic_close
import whiskr.features.add_edit.generated.resources.name_label
import whiskr.features.add_edit.generated.resources.save

@Composable
fun EditProfileScreen(
    component: EditProfileComponent
) {
    val model by component.model.subscribeAsState()
    val focusManager = LocalFocusManager.current
    val isTablet = LocalIsTablet.current

    AdaptiveLayoutWithDialog(
        isTablet = isTablet,
        topBar = {
            SimpleTopBar(
                icon = painterResource(Res.drawable.ic_close),
                onIconClick = component::onBackClicked,
                title = {
                    Text(
                        text = stringResource(Res.string.edit_profile_title),
                        style = WhiskrTheme.typography.caption,
                        color = WhiskrTheme.colors.onBackground
                    )
                }
            )
        }
    ) {
        Text(
            text = stringResource(Res.string.edit_profile_title),
            style = WhiskrTheme.typography.h1.copy(fontSize = if (isTablet) 24.sp else 36.sp),
            color = WhiskrTheme.colors.onBackground,
            textAlign = if (isTablet) TextAlign.Center else TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        ProfilePhotoSelector(
            avatarBytes = model.avatarBytes,
            avatarUrl = model.currentAvatarUrl,
            onAvatarSelected = component::onAvatarSelected
        )

        Text(
            text = stringResource(Res.string.name_label),
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        WhiskrTextField(
            value = model.name,
            onValueChange = component::onNameChanged,
            hint = "John Doe",
            enabled = !model.isLoading,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Text(
            text = stringResource(Res.string.handle_label),
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        WhiskrTextField(
            value = model.handle,
            onValueChange = component::onHandleChanged,
            hint = "@johndoe",
            visualTransformation = HandleVisualTransformation(),
            enabled = !model.isLoading,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Text(
            text = stringResource(Res.string.bio_label),
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        WhiskrTextField(
            value = model.bio,
            onValueChange = component::onBioChanged,
            hint = stringResource(Res.string.bio_hint),
            enabled = !model.isLoading,
            imeAction = ImeAction.Done,
            onImeAction = { focusManager.clearFocus() }
        )

        Spacer(if (isTablet) Modifier.height(16.dp) else Modifier.weight(1f))

        WhiskrButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.save),
            onClick = component::onSaveClicked,
            enabled = !model.isLoading,
            contentColor = Color.White,
            isLoading = model.isLoading
        )
    }
}

