package org.example.whiskr.delegates

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PostInputDelegate(private val scope: CoroutineScope) {
    data class State(
        val text: String = "",
        val files: List<KmpFile> = emptyList(),
        val attachedUrls: List<String> = emptyList(),
        val isSending: Boolean = false
    )

    private val _state = MutableValue(State())
    val state: Value<State> = _state

    fun onTextChanged(text: String) {
        _state.update { it.copy(text = text) }
    }

    fun setInitialContent(imageUrl: String?) {
        if (imageUrl != null) {
            _state.update { it.copy(attachedUrls = listOf(imageUrl)) }
        }
    }

    fun onMediaSelected(newFiles: List<KmpFile>) {
        _state.update { state ->
            val usedSlots = state.files.size + state.attachedUrls.size
            val availableSlots = 10 - usedSlots

            if (availableSlots <= 0) return@update state
            state.copy(files = state.files + newFiles.take(availableSlots))
        }
    }

    fun onRemoveFile(file: KmpFile) {
        _state.update { it.copy(files = it.files - file) }
    }

    fun onRemoveUrl(url: String) {
        _state.update { it.copy(attachedUrls = it.attachedUrls - url) }
    }

    fun <T> submit(
        action: suspend (text: String?, files: List<KmpFile>, urls: List<String>) -> Result<T>,
        onSuccess: (T) -> Unit
    ) {
        val currentState = _state.value
        val hasContent = currentState.text.isNotBlank() || currentState.files.isNotEmpty() || currentState.attachedUrls.isNotEmpty()

        if (!hasContent || currentState.isSending) return

        scope.launch {
            _state.update { it.copy(isSending = true) }

            val textToSend = currentState.text.trim().ifBlank { null }

            action(textToSend, currentState.files, currentState.attachedUrls)
                .onSuccess { result ->
                    _state.update { it.copy(isSending = false, text = "", files = emptyList(), attachedUrls = emptyList()) }
                    onSuccess(result)
                }
                .onFailure { error ->
                    Logger.e(error) { "Failed to send content" }
                    _state.update { it.copy(isSending = false) }
                }
        }
    }
}