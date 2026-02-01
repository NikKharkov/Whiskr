package screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.add_edit.generated.resources.Res
import whiskr.features.add_edit.generated.resources.birthdatepicker_hint
import whiskr.features.add_edit.generated.resources.cancel
import whiskr.features.add_edit.generated.resources.ic_calendar
import whiskr.features.add_edit.generated.resources.ok
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun BirthDatePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val textValue = selectedDate?.let {
        "${it.day.toString().padStart(2, '0')}.${
            it.month.number.toString().padStart(2, '0')
        }.${it.year}"
    } ?: ""

    val pastDatesOnlyValidator = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val now = Clock.System.now().toEpochMilliseconds()
                return utcTimeMillis <= now
            }

            override fun isSelectableYear(year: Int): Boolean {
                val currentYear = Clock.System.now().toLocalDateTime(TimeZone.UTC).year
                return year <= currentYear
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.atStartOfDayIn(TimeZone.UTC)
            ?.toEpochMilliseconds(),
        selectableDates = pastDatesOnlyValidator
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC).date
                            onDateSelected(date)
                        }
                        showDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.ok),
                        color = WhiskrTheme.colors.primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        text = stringResource(Res.string.cancel),
                        color = WhiskrTheme.colors.secondary
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = WhiskrTheme.colors.surface
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    navigationContentColor = WhiskrTheme.colors.onBackground,
                    containerColor = WhiskrTheme.colors.surface,
                    titleContentColor = WhiskrTheme.colors.onBackground,
                    headlineContentColor = WhiskrTheme.colors.onBackground,
                    weekdayContentColor = WhiskrTheme.colors.secondary,
                    subheadContentColor = WhiskrTheme.colors.onBackground,
                    yearContentColor = WhiskrTheme.colors.onBackground,
                    currentYearContentColor = WhiskrTheme.colors.primary,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = WhiskrTheme.colors.primary,
                    dayContentColor = WhiskrTheme.colors.onBackground,
                    disabledDayContentColor = WhiskrTheme.colors.secondary.copy(alpha = 0.5f),
                    selectedDayContainerColor = WhiskrTheme.colors.primary,
                    selectedDayContentColor = Color.White,
                    todayContentColor = WhiskrTheme.colors.primary,
                    todayDateBorderColor = WhiskrTheme.colors.primary
                )
            )
        }
    }

    Box(modifier = modifier) {
        WhiskrTextField(
            value = textValue,
            onValueChange = {},
            hint = stringResource(Res.string.birthdatepicker_hint),
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_calendar),
                    contentDescription = null,
                    tint = WhiskrTheme.colors.outline,
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .customClickable(
                    onClick = { showDialog = true }
                )
        )
    }
}