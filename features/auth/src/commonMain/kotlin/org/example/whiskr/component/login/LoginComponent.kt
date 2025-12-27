package org.example.whiskr.component.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface LoginComponent {
    val model: Value<Model>

    fun onEmailChanged(email: String)

    fun onNextClicked()

    fun onBackClicked()

    data class Model(
        val email: String = "",
        val isNextButtonEnabled: Boolean = false,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseClick: () -> Unit,
            onNavigateToVerify: (email: String) -> Unit,
        ): LoginComponent
    }
}
