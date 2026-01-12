package org.example.whiskr.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.useContents
import org.jetbrains.skia.Image
import platform.CoreGraphics.CGSize
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
class IosScreenCapturer(private val controller: UIViewController) : ScreenCapturer {
    override suspend fun capture(): ImageBitmap? {
        val view = controller.view

        val (width, height) = view.bounds.useContents {
            size.width to size.height
        }

        val sizeValue = cValue<CGSize> {
            this.width = width
            this.height = height
        }

        UIGraphicsBeginImageContextWithOptions(sizeValue, false, 0.0)
        view.drawViewHierarchyInRect(view.bounds, afterScreenUpdates = true)
        val uiImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        return uiImage?.let { image ->
            val pngData = UIImagePNGRepresentation(image) ?: return@let null

            val byteArray = ByteArray(pngData.length.toInt())
            pngData.bytes?.readBytes(pngData.length.toInt())?.copyInto(byteArray)
            try {
                val skiaImage = Image.makeFromEncoded(byteArray)
                skiaImage.toComposeImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

@Composable
actual fun rememberScreenCapturer(): ScreenCapturer {
    val controller = LocalUIViewController.current
    return remember(controller) { IosScreenCapturer(controller) }
}