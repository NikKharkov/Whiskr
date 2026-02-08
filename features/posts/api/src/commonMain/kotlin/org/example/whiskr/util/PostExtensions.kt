package org.example.whiskr.util

import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.getName
import org.example.whiskr.data.MediaType
import org.example.whiskr.data.Media

fun KmpFile.isVideo(context: PlatformContext): Boolean {
    val name = this.getName(context)?.lowercase() ?: ""
    return name.endsWith(".mp4") || name.endsWith(".mov") || name.endsWith(".avi")
}

fun String.toAiPostMedia(): Media {
    return Media(
        id = -1L,
        url = this,
        type = MediaType.IMAGE,
        width = 1024,
        height = 1024,
        thumbnailUrl = this,
        duration = null
    )
}