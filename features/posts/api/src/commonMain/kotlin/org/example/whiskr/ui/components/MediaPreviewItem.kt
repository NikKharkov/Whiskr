package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.compose.AsyncImage
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.getPath
import org.example.whiskr.util.isVideo

@Composable
fun MediaPreviewItem(
    modifier: Modifier = Modifier,
    file: KmpFile,
    onRemove: () -> Unit
) {
    val context = LocalPlatformContext.current

    Box(modifier = modifier) {
        if (file.isVideo(context)) {
            val videoUrl = file.getPath(context) ?: ""

            VideoPlayerComposable(
                modifier = Modifier.fillMaxSize(),
                playerHost = MediaPlayerHost(
                    mediaUrl = videoUrl,
                    isMuted = true,
                    autoPlay = true
                ),
                playerConfig = VideoPlayerConfig(
                    loadingIndicatorColor = Color.Transparent,
                    isAutoHideControlEnabled = true,
                    controlHideIntervalSeconds = 2,
                    isSeekBarVisible = true,
                    isDurationVisible = true,
                    isFastForwardBackwardEnabled = false,
                    isSpeedControlEnabled = false,
                    isFullScreenEnabled = false,
                    isScreenLockEnabled = false,
                    isScreenResizeEnabled = false,
                    isZoomEnabled = false,
                    isMuteControlEnabled = true,
                    iconsTintColor = Color.White,
                    reelVerticalScrolling = true
                ),
            )
        } else {
            AsyncImage(
                model = file,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        IconDelete(
            onRemove = onRemove,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}