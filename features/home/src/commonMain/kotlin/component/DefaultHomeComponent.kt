package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope

@Inject
class DefaultHomeComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onSignOut: () -> Unit,
    @Assisted private val fetchUserProfile: suspend () -> Unit
) : HomeComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableValue(HomeComponent.Model())
    override val model: Value<HomeComponent.Model> = _model

    override fun onRetry() {
        scope.launch {
            fetchUserProfile()
        }
    }
}