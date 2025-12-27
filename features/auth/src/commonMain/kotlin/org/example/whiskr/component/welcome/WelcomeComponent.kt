package org.example.whiskr.component.welcome

import com.arkivanov.decompose.ComponentContext
import dev.gitlive.firebase.auth.FirebaseUser

interface WelcomeComponent {
    fun onAuthResult(result: Result<FirebaseUser?>)

    fun onLoginClicked()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNavigateToLogin: () -> Unit,
            onNavigateToMain: () -> Unit,
        ): WelcomeComponent
    }
}
