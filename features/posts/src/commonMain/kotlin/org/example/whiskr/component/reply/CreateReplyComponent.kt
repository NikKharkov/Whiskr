package org.example.whiskr.component.reply

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.dto.Post

interface CreateReplyComponent {
    val model: Value<Model>

    fun onTextChanged(text: String)
    fun onSendClick()
    fun onBackClick()

    data class Model(
        val targetPost: Post,
        val text: String = "",
        val isSending: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            targetPost: Post,
            onReplyCreated: (Post) -> Unit,
            onBack: () -> Unit
        ): CreateReplyComponent
    }
}