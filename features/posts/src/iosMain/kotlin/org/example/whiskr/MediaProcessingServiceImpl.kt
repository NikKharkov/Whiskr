package org.example.whiskr

import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.useContents
import kotlinx.cinterop.value
import org.example.whiskr.domain.MediaProcessingService
import org.example.whiskr.domain.ProcessedImage
import org.example.whiskr.domain.ProcessedVideo
import platform.AVFoundation.AVAsset
import platform.AVFoundation.AVAssetExportPresetMediumQuality
import platform.AVFoundation.AVAssetExportSession
import platform.AVFoundation.AVAssetExportSessionStatusCompleted
import platform.AVFoundation.AVAssetExportSessionStatusFailed
import platform.AVFoundation.AVAssetImageGenerator
import platform.AVFoundation.AVAssetTrack
import platform.AVFoundation.AVFileTypeMPEG4
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVURLAsset
import platform.AVFoundation.naturalSize
import platform.AVFoundation.tracksWithMediaType
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMake
import platform.Foundation.NSData
import platform.Foundation.NSDataReadingMappedIfSafe
import platform.Foundation.NSError
import platform.Foundation.NSFileCoordinator
import platform.Foundation.NSFileCoordinatorReadingWithoutChanges
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
class MediaProcessingServiceImpl : MediaProcessingService {

    override suspend fun processImage(file: KmpFile): ProcessedImage {
        val nsUrl = file.url
        println("iOS_DEBUG: Processing image at: ${nsUrl.path}")

        val options = NSDataReadingMappedIfSafe

        var data: NSData? = null
        var error: NSError? = null

        // Попытка 1: Чтение с security scope
        val isSecured = nsUrl.startAccessingSecurityScopedResource()
        try {
            memScoped {
                val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                data = NSData.dataWithContentsOfURL(nsUrl, options, errorPtr.ptr)
                error = errorPtr.value
            }
        } finally {
            if (isSecured) nsUrl.stopAccessingSecurityScopedResource()
        }

        // Попытка 2: Если не вышло, пробуем просто file path (иногда URL security мешает)
        if (data == null) {
            println("iOS_DEBUG: First read failed: ${error?.localizedDescription}. Trying simple path read.")
            val path = nsUrl.path
            if (path != null && NSFileManager.defaultManager.fileExistsAtPath(path)) {
                data = NSData.dataWithContentsOfFile(path)
            } else {
                println("iOS_DEBUG: File does not exist at path: $path")
            }
        }

        val finalData = data ?: throw Exception("Cannot read data from ${nsUrl.absoluteString}. Error: ${error?.localizedDescription}")

        val image = UIImage.imageWithData(finalData)
            ?: throw Exception("Cannot decode image")

        val jpegData = UIImageJPEGRepresentation(image, 0.8)
            ?: throw Exception("Compression failed")

        return ProcessedImage(
            bytes = jpegData.toByteArray(),
            width = image.size.useContents { width }.toInt(),
            height = image.size.useContents { height }.toInt()
        )
    }

    override suspend fun processVideo(file: KmpFile): ProcessedVideo {
        // То же самое для видео
        val safeUrl = copyFileSecurely(file.url, isVideo = true)

        try {
            val asset = AVURLAsset(uRL = safeUrl, options = null)
            val durationSeconds = CMTimeGetSeconds(asset.duration).toInt()

            // ... (твоя логика треков и размеров без изменений) ...
            val tracks = asset.tracksWithMediaType(AVMediaTypeVideo)
            val track = tracks.firstOrNull() as? AVAssetTrack
                ?: throw Exception("No video track found")
            val size = track.naturalSize.useContents { this }
            val width = size.width.toInt()
            val height = size.height.toInt()

            // Генерация тамбнейла
            val generator = AVAssetImageGenerator(asset).apply {
                appliesPreferredTrackTransform = true
                requestedTimeToleranceAfter = CMTimeMake(value = 0, timescale = 1)
                requestedTimeToleranceBefore = CMTimeMake(value = 0, timescale = 1)
            }
            val time = CMTimeMake(value = 1, timescale = 1)
            val cgImage = generator.copyCGImageAtTime(time, null, null)
                ?: throw Exception("Failed thumbnail")
            val uiImage = UIImage(cgImage)
            val thumbData = UIImageJPEGRepresentation(uiImage, 0.7)

            val thumbnail = ProcessedImage(
                bytes = thumbData?.toByteArray() ?: ByteArray(0),
                width = width,
                height = height
            )

            // Сжатие
            val compressedPath = compressVideo(asset)

            return ProcessedVideo(
                filePath = compressedPath,
                thumbnail = thumbnail,
                durationSeconds = durationSeconds,
                width = width,
                height = height
            )
        } finally {
            NSFileManager.defaultManager.removeItemAtURL(safeUrl, null)
        }
    }

    /**
     * МАГИЯ ЗДЕСЬ: NSFileCoordinator
     * Эта функция синхронно блокирует удаление файла системой и копирует его к нам.
     */
    private fun copyFileSecurely(sourceUrl: NSURL, isVideo: Boolean): NSURL {
        val fileManager = NSFileManager.defaultManager
        val tempDir = NSURL.fileURLWithPath(NSTemporaryDirectory())
        val ext = sourceUrl.pathExtension ?: if(isVideo) "mp4" else "jpg"
        val destUrl = tempDir.URLByAppendingPathComponent("proc_${NSUUID().UUIDString}.$ext")!!

        // 1. Пытаемся открыть доступ (на всякий случай)
        val isSecured = sourceUrl.startAccessingSecurityScopedResource()

        // 2. Используем координатор
        val coordinator = NSFileCoordinator(filePresenter = null)
        var error: NSError? = null
        var copySucceeded = false

        // Мем-скоп для указателей ObjC
        memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()

            // coordinateReadingItemAtURL "замораживает" файл
            coordinator.coordinateReadingItemAtURL(
                url = sourceUrl,
                options = NSFileCoordinatorReadingWithoutChanges,
                error = errorPtr.ptr
            ) { newUrl ->
                // Внутри этого блока файл ГАРАНТИРОВАННО существует
                // newUrl - это безопасная ссылка, которую дает система
                val actualUrl = newUrl ?: sourceUrl

                val copyError = alloc<ObjCObjectVar<NSError?>>()
                copySucceeded = fileManager.copyItemAtURL(actualUrl, destUrl, copyError.ptr)

                if (!copySucceeded) {
                    println("iOS_COPY_ERROR: ${copyError.value?.localizedDescription}")
                }
            }
            error = errorPtr.value
        }

        if (isSecured) {
            sourceUrl.stopAccessingSecurityScopedResource()
        }

        if (!copySucceeded) {
            // Если координатор не сработал (бывает с очень старыми путями),
            // пробуем "грязный" хак - просто копируем напрямую
            // Но обычно координатор решает проблему.
            try {
                fileManager.copyItemAtURL(sourceUrl, destUrl, null)
            } catch (e: Exception) {
                throw Exception("Failed to secure copy file: ${error?.localizedDescription ?: e.message}")
            }
        }

        return destUrl
    }

    override fun readFile(path: String): ByteArray {
        val data = NSData.Companion.dataWithContentsOfFile(path)
            ?: throw Exception("File not found at $path")
        return data.toByteArray()
    }

    private suspend fun compressVideo(asset: AVAsset): String = suspendCoroutine { continuation ->
        val preset = AVAssetExportPresetMediumQuality
        val session = AVAssetExportSession(asset, preset)

        val fileName = "${NSUUID().UUIDString}.mp4"
        val tempDir = NSTemporaryDirectory()
        val outputUrl = NSURL.Companion.fileURLWithPath(tempDir + fileName)

        session.outputURL = outputUrl
        session.outputFileType = AVFileTypeMPEG4
        session.shouldOptimizeForNetworkUse = true

        session.exportAsynchronouslyWithCompletionHandler {
            when (session.status) {
                AVAssetExportSessionStatusCompleted -> {
                    continuation.resume(outputUrl.path ?: "")
                }

                AVAssetExportSessionStatusFailed -> {
                    continuation.resumeWithException(Exception("Export failed: ${session.error?.localizedDescription}"))
                }

                else -> {
                    continuation.resumeWithException(Exception("Export cancelled or unknown"))
                }
            }
        }
    }

    private fun NSData.toByteArray(): ByteArray {
        val length = this.length.toInt()
        if (length == 0) return ByteArray(0)

        val ptr = this.bytes?.reinterpret<ByteVar>() ?: return ByteArray(0)

        return ptr.readBytes(length)
    }
}