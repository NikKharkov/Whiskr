package org.example.whiskr.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

interface ScreenCapturer {
    suspend fun capture(): ImageBitmap?
}

@Composable
expect fun rememberScreenCapturer(): ScreenCapturer