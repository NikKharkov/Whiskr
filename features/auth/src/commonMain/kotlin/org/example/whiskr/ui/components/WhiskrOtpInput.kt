package org.example.whiskr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.whiskr.theme.WhiskrTheme

private const val OTP_LENGTH = 6

@Composable
fun WhiskrOtpInput(
    code: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = code,
        onValueChange = {
            if (it.length <= OTP_LENGTH && it.all { char -> char.isDigit() }) {
                onValueChange(it)
                if (it.length == OTP_LENGTH) {
                    keyboardController?.hide()
                }
            }
        },
        cursorBrush = SolidColor(Color.Transparent),
        textStyle = TextStyle(color = Color.Transparent),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = { keyboardController?.hide() },
            ),
        modifier = modifier.focusRequester(focusRequester),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth(),
            ) {
                repeat(OTP_LENGTH) { index ->
                    val char = code.getOrNull(index)
                    val isFocused = code.length == index

                    OtpCharBox(
                        text = char?.toString() ?: "",
                        isFocused = isFocused && enabled,
                        isError = errorMessage != null,
                        onClick = {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        },
                    )
                }
            }
        },
    )
}

@Composable
private fun OtpCharBox(
    text: String,
    isFocused: Boolean,
    isError: Boolean,
    onClick: () -> Unit,
) {
    val borderColor =
        when {
            isError -> WhiskrTheme.colors.error
            isFocused -> WhiskrTheme.colors.primary
            else -> WhiskrTheme.colors.secondary
        }

    val borderWidth = if (isFocused) 2.dp else 1.dp

    Box(
        modifier =
            Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(WhiskrTheme.colors.background)
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp),
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = WhiskrTheme.typography.h1.copy(fontSize = 30.sp),
            color = if (isError) WhiskrTheme.colors.error else WhiskrTheme.colors.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}
