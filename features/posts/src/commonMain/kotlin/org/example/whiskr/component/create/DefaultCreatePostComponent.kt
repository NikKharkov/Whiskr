package org.example.whiskr.component.create

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
class DefaultCreatePostComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted private val initialImageUrl: String?,
    @Assisted private val onPostCreated: (Post) -> Unit,
    @Assisted private val onBack: () -> Unit,
    private val postRepository: PostRepository
) : CreatePostComponent, ComponentContext by componentContext {

    private val inputDelegate = PostInputDelegate(componentScope())

    init {
        inputDelegate.setInitialContent(initialImageUrl)
    }

    override val model: Value<CreatePostComponent.Model> = inputDelegate.state.map { inputState ->
        CreatePostComponent.Model(
            text = inputState.text,
            files = inputState.files,
            attachedAiMediaUrls = inputState.attachedUrls,
            isSending = inputState.isSending
        )
    }

    override fun onTextChanged(text: String) = inputDelegate.onTextChanged(text)
    override fun onMediaSelected(files: List<KmpFile>) = inputDelegate.onMediaSelected(files)
    override fun onRemoveFile(file: KmpFile) = inputDelegate.onRemoveFile(file)
    override fun onRemoveUrl(url: String) = inputDelegate.onRemoveUrl(url)

    override fun onBackClick() = onBack()

    override fun onSendClick(context: PlatformContext) {
        inputDelegate.submit(
            action = { text, files, urls ->
                postRepository.createPost(context,text, files, urls)
            },
            onSuccess = onPostCreated
        )
    }
}