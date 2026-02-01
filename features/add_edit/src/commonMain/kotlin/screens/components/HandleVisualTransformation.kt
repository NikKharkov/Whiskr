package screens.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class HandleVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = "@" + text.text

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset + 1

            override fun transformedToOriginal(offset: Int): Int {
                return if (offset > 0) offset - 1 else 0
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}