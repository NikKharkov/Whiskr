package ui.components.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.ChatAttachmentDto
import org.example.whiskr.components.MediaCarousel
import org.example.whiskr.components.MediaSource
import org.example.whiskr.components.VideoPlayerMode

@Composable
fun ChatMediaCarousel(
    attachments: List<ChatAttachmentDto>,
    onMediaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember(attachments) {
        attachments.map {
            MediaSource.Url(it.url, it.type == "VIDEO")
        }
    }

    MediaCarousel(
        items = items,
        onItemClick = onMediaClick,
        videoMode = VideoPlayerMode.CHAT_BUBBLE,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
    )
}
