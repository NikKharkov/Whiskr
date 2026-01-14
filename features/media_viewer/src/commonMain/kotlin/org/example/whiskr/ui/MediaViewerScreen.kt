package org.example.whiskr.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import coil3.compose.AsyncImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import org.example.whiskr.component.MediaViewerComponent
import org.example.whiskr.components.SimpleTopBar
import org.example.whiskr.dto.MediaType
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.util.toCloudStorageUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.media_viewer.generated.resources.Res
import whiskr.features.media_viewer.generated.resources.download
import whiskr.features.media_viewer.generated.resources.ic_close
import whiskr.features.media_viewer.generated.resources.ic_download

@Composable
fun MediaViewerScreen(
    component: MediaViewerComponent
) {
    val pagerState = rememberPagerState(
        initialPage = component.initialIndex,
        pageCount = { component.mediaList.size }
    )

    var isUiVisible by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isUiVisible = !isUiVisible
            },
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true
        ) { page ->
            val media = component.mediaList[page]
            val isPageActive = pagerState.settledPage == page

            val mediaAspectRatio = remember(media.width, media.height) {
                if (media.height > 0) media.width.toFloat() / media.height.toFloat() else 1f
            }

            if (media.type == MediaType.VIDEO) {
                val url = media.url.toCloudStorageUrl()

                val playerHost = remember(url) {
                    MediaPlayerHost(
                        mediaUrl = url,
                        isMuted = false,
                        isPaused = true
                    )
                }

                LaunchedEffect(isPageActive) {
                    if (isPageActive) playerHost.play() else playerHost.pause()
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    VideoPlayerComposable(
                        playerHost = playerHost,
                        playerConfig = VideoPlayerConfig(
                            loadingIndicatorColor = Color.White,
                            controlHideIntervalSeconds = 2,
                            isFastForwardBackwardEnabled = false,
                            isSpeedControlEnabled = false,
                            isFullScreenEnabled = false,
                            isScreenLockEnabled = false,
                            isScreenResizeEnabled = false,
                            isZoomEnabled = false,
                            isSeekBarVisible = true,
                            isDurationVisible = true,
                            isPauseResumeEnabled = true,
                            iconsTintColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(mediaAspectRatio)
                    )
                }
            } else {
                val zoomState = rememberZoomState()

                AsyncImage(
                    model = media.url.toCloudStorageUrl(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(
                            zoomState = zoomState,
                            onTap = { isUiVisible = !isUiVisible }
                        ),
                    contentScale = ContentScale.Fit
                )
            }
        }

        AnimatedVisibility(
            visible = isUiVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            SimpleTopBar(
                title = {
                    Text(
                        "${pagerState.currentPage + 1} / ${component.mediaList.size}",
                        color = Color.White
                    )
                },
                icon = painterResource(Res.drawable.ic_close),
                onIconClick = component::onBackClicked,
                iconTint = Color.White
            )
        }

        AnimatedVisibility(
            visible = isUiVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_download),
                contentDescription = stringResource(Res.string.download),
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .customClickable(onClick = {
                        val currentMedia = component.mediaList[pagerState.currentPage]

                        component.onDownloadClicked(
                            url = currentMedia.url.toCloudStorageUrl(),
                            type = currentMedia.type
                        )
                    })
            )
        }
    }
}
