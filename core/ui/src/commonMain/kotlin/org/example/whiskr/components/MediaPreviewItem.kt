package org.example.whiskr.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.core.PlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.io.getPath

@Composable
fun MediaPreviewItem(
    file: KmpFile,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalPlatformContext.current
    val isVideo = file.isVideo(context)

    Box(modifier = modifier) {
        if (isVideo) {
            val path = file.getPath(context) ?: ""
            VideoPlayer(
                url = path,
                mode = VideoPlayerMode.PREVIEW_EDITOR,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = file,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        IconDelete(onRemove = onRemove, modifier = Modifier.align(Alignment.TopEnd))
    }
}

fun KmpFile.isVideo(context: PlatformContext): Boolean {
    val name = this.getName(context)?.lowercase() ?: ""
    return name.endsWith(".mp4") || name.endsWith(".mov") || name.endsWith(".avi")
}