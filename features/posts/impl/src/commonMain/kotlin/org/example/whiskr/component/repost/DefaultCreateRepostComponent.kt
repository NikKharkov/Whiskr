package org.example.whiskr.component.repost

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.CreateRepostComponent
import org.example.whiskr.component.componentScope
import org.example.whiskr.data.Post
import org.example.whiskr.domain.PostRepository

@Inject
class DefaultCreateRepostComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val targetPost: Post,
    @Assisted private val onBack: () -> Unit,
    @Assisted private val onRepostCreated: () -> Unit,
    private val postRepository: PostRepository
) : CreateRepostComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableValue(CreateRepostComponent.Model(targetPost))
    override val model: Value<CreateRepostComponent.Model> = _model

    override fun onTextChanged(text: String) {
        _model.value = _model.value.copy(text = text)
    }

    override fun onRepostClick() {
        if (_model.value.isReposting) return

        scope.launch {
            _model.value = _model.value.copy(isReposting = true)

            val quote = _model.value.text.takeIf { it.isNotBlank() }

            postRepository.createRepost(originalPostId = targetPost.id, quote = quote)
                .onSuccess {
                    onRepostCreated()
                }
                .onFailure { error ->
                    _model.value = _model.value.copy(isReposting = false)
                    Logger.e(error) { error.message ?: "Repost error" }
                }
        }
    }

    override fun onBackClick() = onBack()
}