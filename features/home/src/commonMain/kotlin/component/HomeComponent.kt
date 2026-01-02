package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface HomeComponent {

    val model: Value<Model>

    data class Model(
        val isLoading: Boolean = true,
        val isError: Boolean = false
    )

    fun onRetry()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onSignOut: () -> Unit,
            onRefresh: suspend () -> Unit
        ): HomeComponent
    }
}