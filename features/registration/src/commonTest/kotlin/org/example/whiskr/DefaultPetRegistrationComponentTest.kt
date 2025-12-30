package org.example.whiskr

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import component.pet.DefaultPetRegistrationComponent
import component.pet.PetRegistrationComponent
import data.PetGender
import data.PetType
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import domain.RegistrationRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultPetRegistrationComponentTest {

    private val repository = mock<RegistrationRepository>()
    private val lifecycle = LifecycleRegistry()
    private val context = DefaultComponentContext(lifecycle)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        lifecycle.resume()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Skip Mode`() {
        val component = createComponent()

        component.model.value.isSkipMode shouldBe true
        component.model.value.isSaveEnabled shouldBe false
    }

    @Test
    fun `entering name should disable Skip Mode`() {
        val component = createComponent()

        component.onNameChanged("Barsik")

        component.model.value.isSkipMode shouldBe false
        component.model.value.isSaveEnabled shouldBe false
    }

    @Test
    fun `filling all fields should enable Save`() {
        val component = createComponent()

        component.onNameChanged("Barsik")
        component.onTypeChanged(PetType.CAT)
        component.onGenderChanged(PetGender.MALE)
        component.onBirthDateChanged(LocalDate(2020, 1, 1))

        component.model.value.isSaveEnabled shouldBe true
    }

    @Test
    fun `onSaveClicked should call repository`() = runTest {
        everySuspend { repository.savePet(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val component = createComponent()

        component.onNameChanged("Barsik")
        component.onTypeChanged(PetType.CAT)
        component.onGenderChanged(PetGender.MALE)
        component.onBirthDateChanged(LocalDate(2020, 1, 1))

        component.onSaveClicked()

        advanceUntilIdle()

        verifySuspend(mode = VerifyMode.exactly(1)) {
            repository.savePet(
                name = "Barsik",
                type = PetType.CAT,
                gender = PetGender.MALE,
                birthDate = LocalDate(2020, 1, 1),
                avatar = null
            )
        }
    }

    private fun createComponent(): PetRegistrationComponent {
        return DefaultPetRegistrationComponent(
            componentContext = context,
            onFinished = {},
            onBack = {},
            registrationRepository = repository
        )
    }
}