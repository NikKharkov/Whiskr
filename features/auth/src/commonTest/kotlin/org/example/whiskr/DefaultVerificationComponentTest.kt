package org.example.whiskr

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.whiskr.component.verification.DefaultVerificationComponent
import org.example.whiskr.domain.AuthRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultVerificationComponentTest {
    private val dispatcher = StandardTestDispatcher()
    private val lifecycle = LifecycleRegistry()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
        lifecycle.resume()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `timer counts down and enables resend button`() =
        runTest(dispatcher) {
            val authRepository = mock<AuthRepository>()

            val component =
                DefaultVerificationComponent(
                    componentContext = DefaultComponentContext(lifecycle),
                    email = "test@test.com",
                    onBack = {},
                    onVerified = {},
                    authRepository = authRepository,
                )

            runCurrent()

            component.model.value.timerSeconds shouldBe 60
            component.model.value.isResendEnabled shouldBe false

            advanceTimeBy(1000)
            runCurrent()

            component.model.value.timerSeconds shouldBe 59

            advanceTimeBy(59_000)
            runCurrent()

            component.model.value.timerSeconds shouldBe 0
            component.model.value.isResendEnabled shouldBe true
        }

    @Test
    fun `resendClicked then calls repo and restarts timer`() =
        runTest(dispatcher) {
            val authRepository = mock<AuthRepository>()

            everySuspend { authRepository.requestOtp(any()) } returns Result.success(Unit)

            val component =
                DefaultVerificationComponent(
                    componentContext = DefaultComponentContext(lifecycle),
                    email = "test@test.com",
                    onBack = {},
                    onVerified = {},
                    authRepository = authRepository,
                )

            runCurrent()
            advanceTimeBy(60_000)
            runCurrent()

            component.model.value.isResendEnabled shouldBe true

            component.onResendClicked()
            runCurrent()

            verifySuspend { authRepository.requestOtp("test@test.com") }

            component.model.value.timerSeconds shouldBe 60
            component.model.value.isResendEnabled shouldBe false
        }

    @Test
    fun `verify clicked then repo fails error shows and stops loading`() =
        runTest(dispatcher) {
            val authRepository = mock<AuthRepository>()

            everySuspend {
                authRepository.verifyOtp(
                    any(),
                    any(),
                )
            } returns Result.failure(Exception("Wrong code"))

            val component =
                DefaultVerificationComponent(
                    componentContext = DefaultComponentContext(lifecycle),
                    email = "test@test.com",
                    onBack = {},
                    onVerified = {},
                    authRepository = authRepository,
                )

            component.onCodeChanged("123456")
            component.onVerifyClicked()

            runCurrent()

            component.model.value.isLoading shouldBe false
            component.model.value.error shouldBe "Invalid token"
        }

    @Test
    fun `code input then length validation and error cleaning`() =
        runTest(dispatcher) {
            val authRepository = mock<AuthRepository>()

            val component =
                DefaultVerificationComponent(
                    componentContext = DefaultComponentContext(lifecycle),
                    email = "test@test.com",
                    onBack = {},
                    onVerified = {},
                    authRepository = authRepository,
                )

            component.onCodeChanged("12345")
            component.model.value.isVerifyEnabled shouldBe false

            component.onCodeChanged("123456")
            component.model.value.isVerifyEnabled shouldBe true

            everySuspend { authRepository.verifyOtp(any(), any()) } returns Result.failure(Exception("Fail"))
            component.onVerifyClicked()
            runCurrent()

            component.model.value.error shouldNotBe null

            component.onCodeChanged("111111")

            component.model.value.error shouldBe null
        }
}
