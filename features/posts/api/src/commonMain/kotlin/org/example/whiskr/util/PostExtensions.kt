package org.example.whiskr.util

import org.example.whiskr.dto.Media
import org.example.whiskr.dto.MediaType

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