package org.example.whiskr.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun WhiskrTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    hint: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    shape: Shape = RoundedCornerShape(24.dp),
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val isError = errorMessage != null

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .widthIn(max = 450.dp)
                .fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = WhiskrTheme.typography.body,
            label = if (label != null) {
                { Text(text = label) }
            } else null,
            placeholder = if (hint != null) {
                { Text(text = hint, color = WhiskrTheme.colors.secondary.copy(alpha = 0.5f)) }
            } else null,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction() },
                onNext = { onImeAction() },
                onSearch = { onImeAction() }
            ),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            shape = shape,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WhiskrTheme.colors.primary,
                unfocusedBorderColor = WhiskrTheme.colors.outline,
                errorBorderColor = WhiskrTheme.colors.error,
                focusedLabelColor = WhiskrTheme.colors.primary,
                unfocusedLabelColor = WhiskrTheme.colors.secondary,
                errorLabelColor = WhiskrTheme.colors.error,
                cursorColor = WhiskrTheme.colors.primary
            )
        )

        AnimatedVisibility(visible = isError) {
            Text(
                text = errorMessage ?: "",
                style = WhiskrTheme.typography.caption,
                color = WhiskrTheme.colors.error,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}