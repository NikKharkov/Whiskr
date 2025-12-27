package org.example.whiskr.component.verification

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class FakeVerificationComponent : VerificationComponent {
    override val model: Value<VerificationComponent.Model> =
        MutableValue(
            VerificationComponent.Model(
                email = "preview@whiskr.com",
                code = "123",
                isVerifyEnabled = false,
                timerSeconds = 59,
                isResendEnabled = false,
                isLoading = false,
            ),
        )

    override fun onCodeChanged(code: String) {}

    override fun onVerifyClicked() {}

    override fun onResendClicked() {}

    override fun onBackClicked() {}
}
