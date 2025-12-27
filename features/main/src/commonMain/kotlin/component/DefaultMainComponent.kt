package component

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import domain.ProfileRepository
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope

@Inject
class DefaultMainComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onSignOut: () -> Unit,
    @Assisted private val onProfileMissing: () -> Unit,
    private val profileRepository: ProfileRepository
) : MainComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableValue(MainComponent.Model())
    override val model: Value<MainComponent.Model> = _model

    init {
        fetchUserProfile()
    }

    override fun onRetry() {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        _model.update { it.copy(isLoading = true, isError = false) }

        scope.launch {
            profileRepository.getMyProfile()
                .onSuccess { profile ->
                    _model.update {
                        it.copy(isLoading = false, userProfile = profile)
                    }
                    Logger.d { "MainComponent: Profile loaded for ${profile.handle}" }
                }
                .onFailure { error ->
                    _model.update { it.copy(isLoading = false) }

                    val msg = error.message.orEmpty()
                    Logger.e(error) { "MainComponent: Failed to fetch profile" }

                    when {
                        msg.contains("401") || msg.contains("Unauthorized") -> {
                            onSignOut()
                        }

                        else -> {
                            _model.update { it.copy(isError = true) }
                        }
                    }
                }
        }
    }
}