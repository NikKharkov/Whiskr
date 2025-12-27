package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import data.ProfileResponseDto

interface MainComponent {

    val model: Value<Model>

    data class Model(
        val isLoading: Boolean = true,
        val userProfile: ProfileResponseDto? = null,
        val isError: Boolean = false
    )

    fun onRetry()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onSignOut: () -> Unit,
            onProfileMissing: () -> Unit
        ): MainComponent
    }
}