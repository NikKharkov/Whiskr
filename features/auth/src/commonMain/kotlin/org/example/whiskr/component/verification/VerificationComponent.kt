package org.example.whiskr.component.verification

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface VerificationComponent {
    val model: Value<Model>

    fun onCodeChanged(code: String)

    fun onVerifyClicked()

    fun onResendClicked()

    fun onBackClicked()

    data class Model(
        val email: String,
        val code: String = "",
        val isVerifyEnabled: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
        val timerSeconds: Int = 60,
        val isResendEnabled: Boolean = false,
    )

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            email: String,
            onBack: () -> Unit,
            onVerified: () -> Unit,
        ): VerificationComponent
    }
}
