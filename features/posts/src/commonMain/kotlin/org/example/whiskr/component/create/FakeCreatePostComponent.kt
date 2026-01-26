package org.example.whiskr.component.create

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile

class FakeCreatePostComponent(
    initialText: String = "",
    initialFiles: List<KmpFile> = emptyList(),
    initialSending: Boolean = false
) : CreatePostComponent {
    override val model: Value<CreatePostComponent.Model> = MutableValue(
        CreatePostComponent.Model(
            text = initialText,
            files = initialFiles,
            isSending = initialSending
        )
    )

    override fun onTextChanged(text: String) {}
    override fun onMediaSelected(files: List<KmpFile>) {}
    override fun onRemoveFile(file: KmpFile) {}
    override fun onSendClick(context: PlatformContext) {}
    override fun onBackClick() {}
}