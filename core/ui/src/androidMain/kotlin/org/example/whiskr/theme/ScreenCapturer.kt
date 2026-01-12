package org.example.whiskr.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidScreenCapturer(
    private val view: View,
    private val window: Window?
) : ScreenCapturer {

    @SuppressLint("UseKtx")
    override suspend fun capture(): ImageBitmap? {
        if (window == null) return null
        if (view.width == 0 || view.height == 0) return null

        return suspendCancellableCoroutine { continuation ->
            try {
                val bitmap = Bitmap.createBitmap(
                    view.width,
                    view.height,
                    Bitmap.Config.ARGB_8888
                )

                val location = IntArray(2)
                view.getLocationInWindow(location)

                val rect = Rect(
                    location[0],
                    location[1],
                    location[0] + view.width,
                    location[1] + view.height
                )

                PixelCopy.request(
                    window,
                    rect,
                    bitmap,
                    { result ->
                        if (result == PixelCopy.SUCCESS) {
                            continuation.resume(bitmap.asImageBitmap())
                        } else {
                            continuation.resume(null)
                        }
                    },
                    Handler(Looper.getMainLooper())
                )
            } catch (e: Exception) {
                e.printStackTrace()
                continuation.resume(null)
            }
        }
    }
}

@Composable
actual fun rememberScreenCapturer(): ScreenCapturer {
    val view = LocalView.current
    val context = view.context
    val window = (context as? Activity)?.window

    return remember(view) { AndroidScreenCapturer(view, window) }
}