package org.example.whiskr.component

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import io.github.vinceglb.filekit.core.FileKit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.example.whiskr.dto.MediaType
import org.example.whiskr.dto.Media
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Inject
class DefaultMediaViewerComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted override val mediaList: List<Media>,
    @Assisted override val initialIndex: Int,
    @Assisted private val onFinished: () -> Unit,
    private val httpClient: HttpClient
) : MediaViewerComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    override fun onBackClicked() = onFinished()

    @OptIn(ExperimentalTime::class)
    override fun onDownloadClicked(url: String, type: MediaType) {
        scope.launch {
            try {
                val bytes: ByteArray = httpClient.get(url).body()

                val ext = if (type == MediaType.VIDEO) "mp4" else "jpg"
                val fileName = "whiskr_${Clock.System.now().toEpochMilliseconds()}"

                FileKit.saveFile(
                    bytes = bytes,
                    baseName = fileName,
                    extension = ext
                )
            } catch (e: Exception) {
                Logger.e(e) { "Download failed: ${e.message}" }
            }
        }
    }
}