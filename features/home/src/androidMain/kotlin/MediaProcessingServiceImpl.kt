import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.AppSpecificStorageConfiguration
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.coroutines.suspendCancellableCoroutine
import org.example.whiskr.domain.MediaProcessingService
import org.example.whiskr.domain.ProcessedImage
import org.example.whiskr.domain.ProcessedVideo
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID
import kotlin.coroutines.resumeWithException
import kotlinx.io.IOException

class MediaProcessingServiceImpl(
    private val context: Context
) : MediaProcessingService {

    override suspend fun processImage(file: KmpFile): ProcessedImage {
        val fileUri = file.uri

        val bitmap: Bitmap = try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, fileUri)

                ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                    val (width, height) = info.size.width to info.size.height
                    val targetSize = 1920

                    if (width > targetSize || height > targetSize) {
                        val scale = minOf(
                            targetSize.toDouble() / width,
                            targetSize.toDouble() / height
                        )
                        decoder.setTargetSize((width * scale).toInt(), (height * scale).toInt())
                    }
                    decoder.isMutableRequired = true
                }
            } else {
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                context.contentResolver.openInputStream(fileUri)?.use { stream ->
                    BitmapFactory.decodeStream(stream, null, options)
                } ?: throw IOException("Input stream is null (check permissions/URI)")

                options.inSampleSize = calculateInSampleSize(options, 1920, 1920)
                options.inJustDecodeBounds = false

                context.contentResolver.openInputStream(fileUri)?.use { stream ->
                    BitmapFactory.decodeStream(stream, null, options)
                } ?: throw IOException("Cannot decode stream on second pass")
            }
        } catch (e: Exception) {
            throw Exception("Failed to process image: ${e.message}")
        }

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

        val w = bitmap.width
        val h = bitmap.height
        bitmap.recycle()

        return ProcessedImage(
            bytes = outputStream.toByteArray(),
            width = w,
            height = h,
            mimeType = "image/jpeg"
        )
    }

    override suspend fun processVideo(file: KmpFile): ProcessedVideo {
        val fileUri = file.uri
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(context, fileUri)

            val widthStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val heightStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            val rotationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

            val rawWidth = widthStr?.toIntOrNull() ?: 0
            val rawHeight = heightStr?.toIntOrNull() ?: 0
            val rotation = rotationStr?.toIntOrNull() ?: 0
            val durationMs = durationStr?.toLongOrNull() ?: 0L

            val (finalW, finalH) = if (rotation == 90 || rotation == 270) {
                rawHeight to rawWidth
            } else {
                rawWidth to rawHeight
            }

            val timeUs = if (durationMs > 1000) 1000000L else 0L
            val thumbBitmap = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                ?: throw Exception("Cannot generate video thumbnail")

            val thumbStream = ByteArrayOutputStream()
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 80, thumbStream)

            val thumbnail = ProcessedImage(
                bytes = thumbStream.toByteArray(),
                width = thumbBitmap.width,
                height = thumbBitmap.height,
                mimeType = "image/jpeg"
            )

            val compressedPath = compressVideo(fileUri)

            return ProcessedVideo(
                filePath = compressedPath,
                thumbnail = thumbnail,
                durationSeconds = (durationMs / 1000).toInt(),
                width = finalW,
                height = finalH,
                mimeType = "video/mp4"
            )
        } finally {
            retriever.release()
        }
    }

    override fun readFile(path: String): ByteArray {
        return File(path).readBytes()
    }

    private suspend fun compressVideo(sourceUri: Uri): String = suspendCancellableCoroutine { continuation ->

        val outputName = "${UUID.randomUUID()}.mp4"
        val outputDir = context.cacheDir

        VideoCompressor.start(
            context = context,
            uris = listOf(sourceUri),
            isStreamable = true,
            storageConfiguration = AppSpecificStorageConfiguration("whiskr_videos"),
            listener = object : CompressionListener {
                override fun onProgress(index: Int, percent: Float) {}

                override fun onStart(index: Int) {}

                override fun onSuccess(index: Int, size: Long, path: String?) {
                    val finalPath = path ?: File(outputDir, outputName).absolutePath
                    if (continuation.isActive) {
                        continuation.resume(finalPath) { cause, _, _ -> continuation.cancel() }
                    }
                }

                override fun onFailure(index: Int, failureMessage: String) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(Exception("Compression failed: $failureMessage"))
                    }
                }

                override fun onCancelled(index: Int) {
                    if (continuation.isActive) {
                        continuation.cancel()
                    }
                }
            },
            configureWith = Configuration(
                quality = VideoQuality.MEDIUM,
                isMinBitrateCheckEnabled = false,
                videoBitrateInMbps = 4,
                disableAudio = false,
                keepOriginalResolution = false,
                videoHeight = 1280.0,
                videoWidth = 720.0,
                videoNames = emptyList()
            )
        )
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}