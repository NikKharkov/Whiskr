package org.example.whiskr.component.welcome

import dev.gitlive.firebase.auth.FirebaseUser

class FakeWelcomeComponent : WelcomeComponent {
    override fun onAuthResult(result: Result<FirebaseUser?>) {}

    override fun onLoginClicked() {}
}
