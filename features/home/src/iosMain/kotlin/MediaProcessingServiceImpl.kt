import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.useContents
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

        val data = NSData.dataWithContentsOfURL(nsUrl) ?: throw Exception("Cannot read data")
        val image = UIImage.imageWithData(data) ?: throw Exception("Cannot decode image")

        val jpegData = UIImageJPEGRepresentation(image, 0.8)
            ?: throw Exception("Compression failed")

        val bytes = jpegData.toByteArray()

        return ProcessedImage(
            bytes = bytes,
            width = image.size.useContents { width }.toInt(),
            height = image.size.useContents { height }.toInt()
        )
    }

    override suspend fun processVideo(file: KmpFile): ProcessedVideo {
        val nsUrl = file.url
        val asset = AVURLAsset(uRL = nsUrl, options = null)

        val durationSeconds = CMTimeGetSeconds(asset.duration).toInt()

        val tracks = asset.tracksWithMediaType(AVMediaTypeVideo)
        val track = tracks.firstOrNull() as? AVAssetTrack
            ?: throw Exception("No video track found")

        val size = track.naturalSize.useContents { this }
        val width = size.width.toInt()
        val height = size.height.toInt()

        val generator = AVAssetImageGenerator(asset).apply {
            appliesPreferredTrackTransform = true
            requestedTimeToleranceAfter = CMTimeMake(value = 0, timescale = 1)
            requestedTimeToleranceBefore = CMTimeMake(value = 0, timescale = 1)
        }

        val time = CMTimeMake(value = 1, timescale = 1)
        val cgImage = generator.copyCGImageAtTime(time, null, null)
            ?: throw Exception("Failed to generate thumbnail")

        val uiImage = UIImage(cgImage)
        val thumbData = UIImageJPEGRepresentation(uiImage, 0.7)
        val thumbBytes = thumbData?.toByteArray() ?: ByteArray(0)

        val thumbnail = ProcessedImage(
            bytes = thumbBytes,
            width = width,
            height = height
        )

        val compressedPath = compressVideo(asset)

        return ProcessedVideo(
            filePath = compressedPath,
            thumbnail = thumbnail,
            durationSeconds = durationSeconds,
            width = width,
            height = height
        )
    }

    override fun readFile(path: String): ByteArray {
        val data = NSData.dataWithContentsOfFile(path)
            ?: throw Exception("File not found at $path")
        return data.toByteArray()
    }

    private suspend fun compressVideo(asset: AVAsset): String = suspendCoroutine { continuation ->
        val preset = AVAssetExportPresetMediumQuality
        val session = AVAssetExportSession(asset, preset)

        val fileName = "${NSUUID().UUIDString}.mp4"
        val tempDir = NSTemporaryDirectory()
        val outputUrl = NSURL.fileURLWithPath(tempDir + fileName)

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
        val byteArray = ByteArray(length)
        if (length > 0) {
            this.bytes!!.readBytes(length).copyInto(byteArray)
        }
        return byteArray
    }
}