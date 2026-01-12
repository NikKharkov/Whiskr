package org.example.whiskr.domain

data class ProcessedVideo(
    val filePath: String,
    val thumbnail: ProcessedImage,
    val durationSeconds: Int,
    val width: Int,
    val height: Int,
    val mimeType: String = "video/mp4"
)
