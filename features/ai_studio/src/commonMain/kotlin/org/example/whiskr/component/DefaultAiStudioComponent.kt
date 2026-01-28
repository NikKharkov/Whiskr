package org.example.whiskr.component

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.decompose.value.update
import io.github.vinceglb.filekit.core.FileKit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.PagingDelegate
import org.example.whiskr.data.AiArtStyle
import org.example.whiskr.data.AiGalleryItemDto
import org.example.whiskr.domain.AiRepository
import org.example.whiskr.share.ImageShareManager
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Inject
class DefaultAiStudioComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted initialBalance: Long,
    @Assisted private val onNavigateToMediaViewer: (String) -> Unit,
    @Assisted private val onNavigateToCreatePost: (String) -> Unit,
    private val aiRepository: AiRepository,
    private val httpClient: HttpClient,
    private val imageShareManager: ImageShareManager
) : AiStudioComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val galleryDelegate = PagingDelegate(
        scope = scope,
        loader = { page ->
            runCatching { aiRepository.getGallery(page) }
        }
    )

    private data class State(
        val balance: Long,
        val prompt: String = "",
        val selectedStyle: AiArtStyle? = null,
        val sourceImageBytes: ByteArray? = null,
        val isGenerating: Boolean = false,
        val error: String? = null,
        val galleryItems: List<AiGalleryItemDto> = emptyList(),
        val isGalleryLoading: Boolean = false
    )

    private val _state = MutableValue(State(balance = initialBalance))

    override val model: Value<AiStudioComponent.Model> = _state.map { s ->
        AiStudioComponent.Model(
            balance = s.balance,
            prompt = s.prompt,
            selectedStyle = s.selectedStyle,
            sourceImageBytes = s.sourceImageBytes,
            isGenerating = s.isGenerating,
            error = s.error,
            galleryItems = s.galleryItems,
            isGalleryLoading = s.isGalleryLoading
        )
    }

    init {
        val observer = { pagingState: PagingDelegate.State<AiGalleryItemDto> ->
            _state.update { current ->
                current.copy(
                    galleryItems = pagingState.items,
                    isGalleryLoading = pagingState.isLoading || pagingState.isLoadingMore
                )
            }
        }
        galleryDelegate.state.subscribe(observer)

        galleryDelegate.firstLoad()
    }

    override fun onPromptChanged(text: String) {
        _state.update { it.copy(prompt = text, error = null) }
    }

    override fun onStyleSelected(style: AiArtStyle?) {
        _state.update { it.copy(selectedStyle = if (it.selectedStyle == style) null else style) }
    }

    override fun onSourceImagePicked(bytes: ByteArray?) {
        _state.update { it.copy(sourceImageBytes = bytes) }
    }

    override fun onShareClicked(url: String) {
        scope.launch {
            try {
                val bytes: ByteArray = httpClient.get(url).body()

                imageShareManager.shareImages(listOf(bytes))
                    .onFailure { e ->
                        Logger.e(e) { "Share execution failed" }
                    }

            } catch (e: Exception) {
                Logger.e(e) { "Download for share failed" }
            }
        }
    }

    override fun onGenerateClicked() {
        val s = _state.value
        if (s.prompt.isBlank() || s.isGenerating) return

        scope.launch {
            _state.update { it.copy(isGenerating = true, error = null) }

            try {
                val response = aiRepository.generateImage(
                    prompt = s.prompt,
                    style = s.selectedStyle,
                    imageBytes = s.sourceImageBytes
                )

                val newItem = AiGalleryItemDto(
                    id = response.id,
                    imageUrl = response.resultImageUrl,
                    prompt = s.prompt,
                    style = s.selectedStyle
                )

                _state.update {
                    it.copy(
                        balance = response.updatedBalance,
                        isGenerating = false,
                        prompt = "",
                        sourceImageBytes = null
                    )
                }

                galleryDelegate.prependItem(newItem)

            } catch (e: Exception) {
                Logger.e(e) { "Generation failed" }
                _state.update { it.copy(isGenerating = false, error = e.message) }
            }
        }
    }

    override fun onGalleryLoadMore() {
        galleryDelegate.loadMore()
    }

    @OptIn(ExperimentalTime::class)
    override fun onDownloadClicked(url: String) {
        scope.launch {
            try {
                val bytes: ByteArray = httpClient.get(url).body()
                val fileName = "whiskr_ai_${Clock.System.now().toEpochMilliseconds()}"
                FileKit.saveFile(bytes = bytes, baseName = fileName, extension = "jpg")
            } catch (e: Exception) {
                Logger.e(e) { "Download failed" }
            }
        }
    }

    override fun onImageClicked(url: String) = onNavigateToMediaViewer(url)
    override fun onPostClicked(url: String) = onNavigateToCreatePost(url)
}