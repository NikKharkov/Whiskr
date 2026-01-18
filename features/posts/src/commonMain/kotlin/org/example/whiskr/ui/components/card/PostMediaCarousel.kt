package org.example.whiskr.ui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.compose.AsyncImage
import org.example.whiskr.dto.MediaType
import org.example.whiskr.dto.PostMedia
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.util.toCloudStorageUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.posts.generated.resources.Res
import whiskr.features.posts.generated.resources.full_screen
import whiskr.features.posts.generated.resources.ic_full_screen

@Composable
fun PostMediaCarousel(
    medias: List<PostMedia>,
    onMediaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (medias.isEmpty()) return

    val pagerState = rememberPagerState { medias.size }

    Box(
        modifier = modifier
            .widthIn(max = 500.dp)
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.Black.copy(alpha = 0.05f))
            .clipToBounds()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            MediaCard(
                media = medias[index],
                shouldBePaused = pagerState.settledPage != index,
                onClick = { onMediaClick(index) }
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
    modifier: Modifier = Modifier,
    media: PostMedia,
    shouldBePaused: Boolean,
    onClick: () -> Unit
) {
    when (media.type) {
        MediaType.IMAGE -> {
            AsyncImage(
                model = media.url.toCloudStorageUrl(),
                contentDescription = null,
                modifier = modifier
                    .fillMaxSize()
                    .customClickable(onClick = onClick),
                contentScale = ContentScale.Crop
            )
        }

        MediaType.VIDEO -> {
            var isMuted by remember { mutableStateOf(true) }

            val videoPlayerHost = remember(media.url, shouldBePaused, isMuted) {
                MediaPlayerHost(
                    mediaUrl = media.url.toCloudStorageUrl(),
                    isPaused = shouldBePaused,
                    isMuted = isMuted
                )
            }

            Box(modifier = modifier.fillMaxSize()) {
                VideoPlayerComposable(
                    playerHost = videoPlayerHost,
                    playerConfig = VideoPlayerConfig(
                        loadingIndicatorColor = Color.Transparent,
                        controlHideIntervalSeconds = 2,
                        isFastForwardBackwardEnabled = false,
                        isSpeedControlEnabled = false,
                        isFullScreenEnabled = false,
                        isScreenLockEnabled = false,
                        isScreenResizeEnabled = false,
                        isZoomEnabled = false,
                        iconsTintColor = Color.White
                    ),
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .padding(bottom = 20.dp, end = 20.dp)
                        .padding(8.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_full_screen),
                        contentDescription = stringResource(Res.string.full_screen),
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .customClickable(onClick = onClick)
                    )
                }
            }
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