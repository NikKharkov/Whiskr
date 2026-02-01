package screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.add_pet.AddPetComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.layouts.AdaptiveLayoutWithDialog
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import screens.components.BirthDatePicker
import screens.components.PetGenderSelector
import screens.components.PetTypeSelector
import screens.components.ProfilePhotoSelector
import whiskr.features.add_edit.generated.resources.Res
import whiskr.features.add_edit.generated.resources.add_pet_title
import whiskr.features.add_edit.generated.resources.ic_close
import whiskr.features.add_edit.generated.resources.name_label
import whiskr.features.add_edit.generated.resources.pet_birthdate_label
import whiskr.features.add_edit.generated.resources.pet_gender_label
import whiskr.features.add_edit.generated.resources.pet_type_label
import whiskr.features.add_edit.generated.resources.save

@Composable
fun AddPetScreen(
    component: AddPetComponent
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
                        text = stringResource(Res.string.add_pet_title),
                        style = WhiskrTheme.typography.caption,
                        color = WhiskrTheme.colors.onBackground
                    )
                }
            )
        }
    ) {
        Text(
            text = stringResource(Res.string.add_pet_title),
            style = WhiskrTheme.typography.h1.copy(fontSize = if (isTablet) 24.sp else 36.sp),
            color = WhiskrTheme.colors.onBackground,
            textAlign = if (isTablet) TextAlign.Center else TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        ProfilePhotoSelector(
            avatarBytes = model.avatarBytes,
            avatarUrl = null,
            onAvatarSelected = component::onAvatarSelected
        )

        Text(
            text = stringResource(Res.string.pet_type_label),
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        PetTypeSelector(
            selectedType = model.type,
            onTypeSelected = component::onTypeChanged
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
            hint = "Luna",
            enabled = !model.isLoading,
            imeAction = ImeAction.Done,
            onImeAction = { focusManager.clearFocus() }
        )

        Text(
            text = stringResource(Res.string.pet_gender_label),
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        PetGenderSelector(
            selectedGender = model.gender,
            onGenderSelected = component::onGenderChanged
        )

        Text(
            text = stringResource(Res.string.pet_birthdate_label),
            style = WhiskrTheme.typography.body,
            color = WhiskrTheme.colors.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        BirthDatePicker(
            selectedDate = model.birthDate,
            onDateSelected = component::onBirthDateChanged
        )

        Spacer(if (isTablet) Modifier.height(16.dp) else Modifier.weight(1f))

        WhiskrButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.save),
            onClick = component::onSaveClicked,
            enabled = model.isSaveEnabled && !model.isLoading,
            contentColor = Color.White,
            isLoading = model.isLoading
        )
    }
}
