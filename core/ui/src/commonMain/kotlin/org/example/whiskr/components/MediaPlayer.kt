package org.example.whiskr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.compose.AsyncImage
import com.mohamedrejeb.calf.io.KmpFile
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.util.toCloudStorageUrl
import org.jetbrains.compose.resources.painterResource
import whiskr.core.ui.generated.resources.Res
import whiskr.core.ui.generated.resources.ic_full_screen

sealed interface MediaSource {
    data class Url(val url: String, val isVideo: Boolean) : MediaSource
    data class File(val file: KmpFile) : MediaSource
}

enum class VideoPlayerMode {
    CHAT_BUBBLE,
    POST_FEED,
    PREVIEW_EDITOR
}

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    url: String,
    mode: VideoPlayerMode,
    onClick: (() -> Unit)? = null
) {
    val config = remember(mode) {
        when (mode) {
            VideoPlayerMode.CHAT_BUBBLE -> VideoPlayerConfig(
                showControls = false,
                isPauseResumeEnabled = false,
                isSeekBarVisible = false,
                isDurationVisible = false,
                isMuteControlEnabled = false,
                isFullScreenEnabled = false,
                isZoomEnabled = false,
                loadingIndicatorColor = Color.White
            )

            VideoPlayerMode.POST_FEED -> VideoPlayerConfig(
                loadingIndicatorColor = Color.Transparent,
                controlHideIntervalSeconds = 2,
                isMuteControlEnabled = true,
                isFullScreenEnabled = false,
                isSeekBarVisible = false,
                showControls = true
            )

            VideoPlayerMode.PREVIEW_EDITOR -> VideoPlayerConfig(
                loadingIndicatorColor = Color.Transparent,
                isAutoHideControlEnabled = true,
                isSeekBarVisible = true,
                isDurationVisible = true,
                isMuteControlEnabled = true
            )
        }
    }

    val host = remember(url) {
        MediaPlayerHost(
            mediaUrl = url,
            isMuted = true,
            autoPlay = mode != VideoPlayerMode.PREVIEW_EDITOR
        )
    }

    Box(modifier = modifier) {
        VideoPlayerComposable(
            modifier = Modifier.fillMaxSize(),
            playerHost = host,
            playerConfig = config
        )

        if (onClick != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = if (mode == VideoPlayerMode.POST_FEED) 40.dp else 0.dp)
                    .customClickable(onClick = onClick)
            )
        }

        if (mode == VideoPlayerMode.POST_FEED && onClick != null) {
            Box(modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)) {
                Icon(
                    painter = painterResource(Res.drawable.ic_full_screen),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp).customClickable(onClick = onClick)
                )
            }
        }
    }
}

@Composable
fun MediaCarousel(
    items: List<MediaSource>,
    onItemClick: (Int) -> Unit,
    videoMode: VideoPlayerMode,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val pagerState = rememberPagerState { items.size }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.1f))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            when (val item = items[index]) {
                is MediaSource.Url -> {
                    if (item.isVideo) {
                        VideoPlayer(
                            url = item.url.toCloudStorageUrl(),
                            mode = videoMode,
                            onClick = { onItemClick(index) }
                        )
                    } else {
                        AsyncImage(
                            model = item.url.toCloudStorageUrl(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().customClickable { onItemClick(index) }
                        )
                    }
                }

                is MediaSource.File -> {

                }
            }
        }

        if (items.size > 1) {
            MediaCounter(
                current = pagerState.currentPage + 1,
                total = items.size,
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            )
        }
    }
}

@Composable
private fun MediaCounter(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$current/$total",
            color = Color.White,
            style = WhiskrTheme.typography.caption
        )
    }
}