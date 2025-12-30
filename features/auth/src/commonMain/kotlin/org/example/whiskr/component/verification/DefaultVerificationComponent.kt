package org.example.whiskr.component.verification

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope
import org.example.whiskr.domain.AuthRepository

@Inject
class DefaultVerificationComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val email: String,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onVerified: (Boolean) -> Unit,
    private val authRepository: AuthRepository,
) : VerificationComponent, ComponentContext by componentContext {
    private val scope = componentScope()

    private val _model = MutableValue(VerificationComponent.Model(email))
    override val model: Value<VerificationComponent.Model> = _model

    init {
        startResendTimer()
    }

    override fun onCodeChanged(code: String) {
        _model.update {
            it.copy(
                code = code,
                error = null,
                isVerifyEnabled = code.length == 6,
            )
        }
    }

    override fun onVerifyClicked() {
        val code = _model.value.code

        _model.update { it.copy(isLoading = true, error = null) }

        scope.launch {
            authRepository.verifyOtp(email, code)
                .onSuccess { isNewUser ->
                    _model.update { it.copy(isLoading = false) }
                    onVerified(isNewUser)
                }
                .onFailure {
                    _model.update {
                        it.copy(
                            isLoading = false,
                            error = "Invalid token",
                        )
                    }
                }
        }
    }

    override fun onResendClicked() {
        if (!_model.value.isResendEnabled) return

        scope.launch {
            authRepository.requestOtp(email)
                .onSuccess {
                    startResendTimer()
                }
                .onFailure { error ->
                    Logger.e(error) { "Failed to resend code: ${error.message}" }
                }
        }
    }

    override fun onBackClicked() = onBack()

    private fun startResendTimer() {
        _model.update { it.copy(isResendEnabled = false, timerSeconds = 60) }

        scope.launch {
            while (_model.value.timerSeconds > 0) {
                delay(1000)
                if (!isActive) break

                _model.update { it.copy(timerSeconds = it.timerSeconds - 1) }
            }
            _model.update { it.copy(isResendEnabled = true) }
        }
    }
}
