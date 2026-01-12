package org.example.whiskr.ui.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.example.whiskr.data.MediaType
import org.example.whiskr.data.PostMedia
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun PostMediaCarousel(
    medias: List<PostMedia>,
    modifier: Modifier = Modifier
) {
    if (medias.isEmpty()) return

    val pagerState = rememberPagerState { medias.size }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.Black.copy(alpha = 0.05f))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            MediaCard(
                media = medias[index],
                shouldBePaused = pagerState.settledPage != index
            )
        }

        if (medias.size > 1) {
            MediaCounter(
                current = pagerState.currentPage + 1,
                total = medias.size,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
private fun MediaCard(
    media: PostMedia,
    shouldBePaused: Boolean
) {
    when(media.type) {
        MediaType.IMAGE -> {
            AsyncImage(
                model = media.url.replace("localhost", "10.0.2.2"),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        MediaType.VIDEO -> {
            var isMuted by remember { mutableStateOf(true) }

            val videoPlayerHost = remember(media.url, shouldBePaused, isMuted) {
                MediaPlayerHost(
                    mediaUrl = media.url.replace("localhost", "10.0.2.2"),
                    isPaused = shouldBePaused,
                    isMuted = isMuted
                )
            }

            VideoPlayerComposable(
                playerHost = videoPlayerHost,
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
                modifier = Modifier.fillMaxSize()
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