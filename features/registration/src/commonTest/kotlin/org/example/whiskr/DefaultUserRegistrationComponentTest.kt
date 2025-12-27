package org.example.whiskr

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import component.user.DefaultUserRegistrationComponent
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import domain.RegistrationRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultUserRegistrationComponentTest {

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
    fun `validate inputs enables and disables submit button`() {
        val repository = mock<RegistrationRepository>()
        val component = DefaultUserRegistrationComponent(
            componentContext = DefaultComponentContext(lifecycle),
            onRegistrationFinished = {},
            repository = repository
        )

        component.model.value.isSubmitEnabled shouldBe false

        component.onNameChanged("John")
        component.model.value.isSubmitEnabled shouldBe false

        component.onUsernameChanged("johndoe")
        component.model.value.isSubmitEnabled shouldBe true

        component.onNameChanged("")
        component.model.value.isSubmitEnabled shouldBe false
    }

    @Test
    fun `success flow calls repo with added @ symbol and navigates`() = runTest(dispatcher) {
        val repository = mock<RegistrationRepository>()

        everySuspend { repository.registerProfile(any(), any(), any()) } returns Result.success(Unit)

        var navigationCalled = false
        val component = DefaultUserRegistrationComponent(
            componentContext = DefaultComponentContext(lifecycle),
            onRegistrationFinished = { navigationCalled = true },
            repository = repository
        )

        component.onNameChanged("John")
        component.onUsernameChanged("johndoe")
        component.onNextClicked()

        runCurrent()

        component.model.value.isLoading shouldBe false

        verifySuspend {
            repository.registerProfile(
                name = "John",
                username = "@johndoe",
                avatar = any()
            )
        }

        navigationCalled shouldBe true
    }

    @Test
    fun `handle taken error parses message correctly`() = runTest(dispatcher) {
        val repository = mock<RegistrationRepository>()
        everySuspend { repository.registerProfile(any(), any(), any()) } returns
                Result.failure(Exception("This handle is already taken by someone"))

        val component = DefaultUserRegistrationComponent(
            componentContext = DefaultComponentContext(lifecycle),
            onRegistrationFinished = {},
            repository = repository
        )

        component.onNameChanged("John")
        component.onUsernameChanged("busy_handle")
        component.onNextClicked()

        runCurrent()

        component.model.value.isLoading shouldBe false
        component.model.value.usernameError shouldBe "Handle already taken"
    }
}