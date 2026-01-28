package org.example.whiskr.share

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.popoverPresentationController

class IosImageShareManager : ImageShareManager {

    override suspend fun shareImages(images: List<ByteArray>): Result<Unit> {
        if (images.isEmpty()) return Result.success(Unit)

        return runCatching {
            val urls = withContext(Dispatchers.Default) {
                images.mapIndexedNotNull { index, bytes ->
                    saveImageToTemp(bytes, "share_image_$index.jpg")
                }
            }

            if (urls.isEmpty()) error("Failed to create temporary files")


            val activityViewController = UIActivityViewController(
                activityItems = urls,
                applicationActivities = null
            )

            val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController
                ?: UIApplication.sharedApplication.windows.firstOrNull { (it as? UIWindow)?.isKeyWindow() == true }
                    ?.let { (it as UIWindow).rootViewController }

            activityViewController.popoverPresentationController?.sourceView = rootController?.view

            rootController?.presentViewController(
                activityViewController,
                animated = true,
                completion = null
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun saveImageToTemp(bytes: ByteArray, fileName: String): NSURL? {
        val tempDir = NSTemporaryDirectory()
        val filePath = tempDir + fileName

        NSFileManager.defaultManager.removeItemAtPath(filePath, error = null)

        return bytes.usePinned { pinned ->
            val nsData = NSData.dataWithBytes(pinned.addressOf(0), bytes.size.toULong())
            if (nsData.writeToFile(filePath, atomically = true)) {
                NSURL.fileURLWithPath(filePath)
            } else null
        }
    }
}