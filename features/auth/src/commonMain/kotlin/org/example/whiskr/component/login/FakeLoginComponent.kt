package org.example.whiskr.component.login

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class FakeLoginComponent : LoginComponent {
    override val model: Value<LoginComponent.Model> =
        MutableValue(
            LoginComponent.Model(
                email = "alex.doe@whiskr.com",
                isNextButtonEnabled = true,
                isLoading = false,
                errorMessage = null,
            ),
        )

    override fun onEmailChanged(email: String) {}

    override fun onNextClicked() {}

    override fun onBackClicked() {}
}
