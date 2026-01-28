package org.example.whiskr.component.reply

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.component.componentScope
import org.example.whiskr.delegates.PostInputDelegate
import org.example.whiskr.domain.PostRepository
import org.example.whiskr.dto.Post

@Inject
class DefaultCreateReplyComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val post: Post,
    @Assisted private val onReplyCreated: (Post) -> Unit,
    @Assisted private val onBack: () -> Unit,
    private val postRepository: PostRepository
) : CreateReplyComponent, ComponentContext by componentContext {

    private val inputDelegate = PostInputDelegate(componentScope())

    override val model: Value<CreateReplyComponent.Model> = inputDelegate.state.map { inputState ->
        CreateReplyComponent.Model(
            targetPost = post,
            text = inputState.text,
            files = inputState.files,
            isSending = inputState.isSending
        )
    }

    override fun onTextChanged(text: String) = inputDelegate.onTextChanged(text)
    override fun onMediaSelected(files: List<KmpFile>) = inputDelegate.onMediaSelected(files)
    override fun onRemoveFile(file: KmpFile) = inputDelegate.onRemoveFile(file)
    override fun onBackClick() = onBack()

    override fun onSendClick(context: PlatformContext) {
        inputDelegate.submit(
            action = { text, files, _ ->
                postRepository.replyToPost(
                    context = context,
                    targetPostId = post.id,
                    text = text ?: "",
                    files = files
                )
            },
            onSuccess = onReplyCreated
        )
    }
}