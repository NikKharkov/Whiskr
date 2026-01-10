package org.example.whiskr.util

import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.getName

fun KmpFile.isVideo(context: PlatformContext): Boolean {
    val name = this.getName(context)?.lowercase() ?: ""
    return name.endsWith(".mp4") || name.endsWith(".mov") || name.endsWith(".avi")
}