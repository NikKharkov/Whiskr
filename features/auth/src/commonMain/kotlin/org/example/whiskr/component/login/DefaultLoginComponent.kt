package org.example.whiskr.component.login

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope
import org.example.whiskr.domain.AuthRepository

@Inject
class DefaultLoginComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBackClick: () -> Unit,
    @Assisted private val onNavigateToVerify: (String) -> Unit,
    private val authRepository: AuthRepository,
) : LoginComponent, ComponentContext by componentContext {
    private val scope = componentScope()

    private val emailValidator = Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$")

    private val _model = MutableValue(LoginComponent.Model())
    override val model: Value<LoginComponent.Model> = _model

    override fun onEmailChanged(email: String) {
        _model.update {
            it.copy(
                email = email,
                errorMessage = null,
                isNextButtonEnabled = emailValidator.matches(email),
            )
        }
    }

    override fun onNextClicked() {
        val currentEmail = _model.value.email

        _model.update { it.copy(isLoading = true, errorMessage = null) }

        scope.launch {
            authRepository.requestOtp(currentEmail)
                .onSuccess {
                    _model.update { it.copy(isLoading = false) }
                    onNavigateToVerify(currentEmail)
                }
                .onFailure { error ->
                    Logger.e(error) { "FAILURE: Request failed" }
                    _model.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Unknown error",
                        )
                    }
                }
        }
    }

    override fun onBackClicked() = onBackClick()
}
