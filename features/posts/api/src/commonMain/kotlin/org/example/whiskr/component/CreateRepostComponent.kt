package org.example.whiskr.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.example.whiskr.data.Post

interface CreateRepostComponent {
    val model: Value<Model>

    fun onBackClick()
    fun onRepostClick()
    fun onTextChanged(text: String)

    data class Model(
        val targetPost: Post,
        val text: String = "",
        val isReposting: Boolean = false
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            targetPost: Post,
            onBack: () -> Unit,
            onRepostCreated: () -> Unit
        ): CreateRepostComponent
    }
}