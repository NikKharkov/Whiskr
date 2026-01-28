package org.example.whiskr.share

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AndroidImageShareManager(private val context: Context) : ImageShareManager {

    override suspend fun shareImages(images: List<ByteArray>): Result<Unit> {
        if (images.isEmpty()) return Result.success(Unit)

        return runCatching {
            withContext(Dispatchers.IO) {
                val uris = images.mapIndexed { index, bytes ->
                    val file = File(context.cacheDir, "share_image_$index.jpg")
                    file.writeBytes(bytes)

                    FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                }

                val intent = if (uris.size == 1) {
                    Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_STREAM, uris.first())
                        type = "image/jpeg"
                    }
                } else {
                    Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                        putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
                        type = "image/jpeg"
                    }
                }

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val chooser = Intent.createChooser(intent, null).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                context.startActivity(chooser)
            }
        }
    }
}