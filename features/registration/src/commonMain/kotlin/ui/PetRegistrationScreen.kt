package ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.pet.FakePetRegistrationComponent
import component.pet.PetRegistrationComponent
import data.PetGender
import data.PetType
import kotlinx.datetime.LocalDate
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.layouts.AdaptiveLayoutWithDialog
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.components.BirthDatePicker
import ui.components.PetGenderSelector
import ui.components.PetTypeSelector
import ui.components.ProfilePhotoSelector
import whiskr.features.registration.generated.resources.Res
import whiskr.features.registration.generated.resources.birthday
import whiskr.features.registration.generated.resources.complete_n_welcome
import whiskr.features.registration.generated.resources.gender
import whiskr.features.registration.generated.resources.ic_arrow_back
import whiskr.features.registration.generated.resources.its_a
import whiskr.features.registration.generated.resources.name
import whiskr.features.registration.generated.resources.pet_registration_caption
import whiskr.features.registration.generated.resources.pet_registration_title
import whiskr.features.registration.generated.resources.skip
import whiskr.features.registration.generated.resources.step2

@Composable
fun PetRegistrationScreen(
    component: PetRegistrationComponent
) {
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
                            text = stringResource(Res.string.step2),
                            style = WhiskrTheme.typography.caption,
                            color = WhiskrTheme.colors.onBackground
                        )
                    }
                )
            }
        ) {
            Text(
                text = stringResource(Res.string.pet_registration_title),
                style = WhiskrTheme.typography.h1.copy(fontSize = if (isTablet) 24.sp else 36.sp),
                color = WhiskrTheme.colors.onBackground,
                textAlign = if (isTablet) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.pet_registration_caption),
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.secondary,
                textAlign = if (isTablet) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            ProfilePhotoSelector(
                avatarBytes = model.avatarBytes,
                onAvatarSelected = component::onAvatarSelected
            )

            Text(
                text = stringResource(Res.string.its_a),
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
                text = stringResource(Res.string.name),
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            WhiskrTextField(
                value = model.name ?: "",
                onValueChange = component::onNameChanged,
                hint = "Luna",
                enabled = !model.isLoading,
                keyboardType = KeyboardType.Text
            )

            Text(
                text = stringResource(Res.string.gender),
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
                text = stringResource(Res.string.birthday),
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
                text = stringResource(if (model.isSkipMode) Res.string.skip else Res.string.complete_n_welcome),
                onClick = {
                    if (model.isSkipMode) {
                        component.onSkipClicked()
                    } else {
                        component.onSaveClicked()
                    }
                },
                enabled = model.isSkipMode || model.isSaveEnabled,
                contentColor = Color.White,
                isLoading = model.isLoading
            )
        }
    }
}

@Preview(name = "Light Mode (Empty/Skip)", showBackground = true)
@Composable
private fun PetRegistrationScreenPreview() {
    WhiskrTheme(isDarkTheme = false) {
        PetRegistrationScreen(
            FakePetRegistrationComponent(
                initialModel = PetRegistrationComponent.Model()
            )
        )
    }
}

@Preview(name = "Dark Mode (Filled)", showBackground = true)
@Composable
private fun PetRegistrationScreenDarkThemePreview() {
    WhiskrTheme(isDarkTheme = true) {
        PetRegistrationScreen(
            FakePetRegistrationComponent(
                initialModel = PetRegistrationComponent.Model(
                    name = "Venom",
                    type = PetType.CAT,
                    gender = PetGender.MALE,
                    birthDate = LocalDate(2023, 10, 31),
                    isSaveEnabled = true,
                    isLoading = false
                )
            )
        )
    }
}

@Preview(name = "Tablet (Loading/Error)", widthDp = 891, showBackground = true)
@Composable
private fun PetRegistrationScreenTabletPreview() {
    WhiskrTheme(isDarkTheme = false) {
        PetRegistrationScreen(
            FakePetRegistrationComponent(
                initialModel = PetRegistrationComponent.Model(
                    name = "Spirit",
                    type = PetType.HORSE,
                    gender = PetGender.MALE,
                    birthDate = LocalDate(2020, 1, 1),
                    isLoading = true,
                    isSaveEnabled = true
                )
            )
        )
    }
}