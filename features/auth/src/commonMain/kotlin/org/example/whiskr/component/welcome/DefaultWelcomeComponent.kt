package org.example.whiskr.component.welcome

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope
import org.example.whiskr.domain.AuthRepository

@Inject
class DefaultWelcomeComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onNavigateToLogin: () -> Unit,
    @Assisted private val onNavigateToMain: () -> Unit,
    private val authRepository: AuthRepository,
) : WelcomeComponent, ComponentContext by componentContext {
    private val scope = componentScope()

    override fun onAuthResult(result: Result<FirebaseUser?>) {
        val user = result.getOrNull()

        if (user != null) {
            scope.launch {
                val userId = user.getIdToken(false) ?: ""

                authRepository.login(userId)
                    .onSuccess {
                        Logger.d { "Login success" }
                        onNavigateToMain()
                    }
                    .onFailure { error ->
                        Logger.e(error) { "Login failed" }
                    }
            }
        } else {
            Logger.d { "Authentication cancelled or failed: ${result.exceptionOrNull()}" }
        }
    }

    override fun onLoginClicked() = onNavigateToLogin()
}
