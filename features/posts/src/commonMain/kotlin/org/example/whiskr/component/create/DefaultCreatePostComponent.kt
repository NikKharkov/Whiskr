package org.example.whiskr.component.create

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.dto.Post

@Inject
class DefaultCreatePostComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onPostCreated: (Post) -> Unit,
    @Assisted private val onBack: () -> Unit,
    private val postRepository: PostRepository
) : CreatePostComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val _model = MutableValue(CreatePostComponent.Model())
    override val model: Value<CreatePostComponent.Model> = _model

    override fun onTextChanged(text: String) {
        _model.update { it.copy(text = text) }
    }

    override fun onMediaSelected(files: List<KmpFile>) {
        _model.update { state ->
            val availableSlots = 10 - state.files.size
            if (availableSlots <= 0) return@update state
            state.copy(files = state.files + files.take(availableSlots))
        }
    }

    override fun onRemoveFile(file: KmpFile) {
        _model.update { it.copy(files = it.files - file) }
    }

    override fun onSendClick(context: PlatformContext) {
        val state = _model.value
        if (state.text.isBlank() && state.files.isEmpty()) return
        if (state.isSending) return

        scope.launch {
            _model.update { it.copy(isSending = true) }

            postRepository.createPost(context, state.text.ifBlank { null }, state.files)
                .onSuccess { newPost ->
                    _model.update { it.copy(isSending = false, text = "", files = emptyList()) }
                    onPostCreated(newPost)
                }
                .onFailure {
                    Logger.e(it) { "Failed to create post" }
                    _model.update { s -> s.copy(isSending = false) }
                }
        }
    }

    override fun onBackClick() = onBack()
}