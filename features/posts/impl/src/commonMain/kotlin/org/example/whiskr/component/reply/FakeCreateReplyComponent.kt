package org.example.whiskr.component.reply

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import org.example.whiskr.component.CreateReplyComponent
import org.example.whiskr.util.mockPostForReply


class FakeCreateReplyComponent(
    initialModel: CreateReplyComponent.Model = CreateReplyComponent.Model(
        targetPost = mockPostForReply
    )
) : CreateReplyComponent {
    override val model: Value<CreateReplyComponent.Model> = MutableValue(initialModel)

    override fun onTextChanged(text: String) {}
    override fun onMediaSelected(files: List<KmpFile>) {}
    override fun onRemoveFile(file: KmpFile) {}
    override fun onSendClick(context: PlatformContext) {}
    override fun onBackClick() {}
}