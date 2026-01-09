package org.example.whiskr.domain

data class ProcessedImage(
    val bytes: ByteArray,
    val width: Int,
    val height: Int,
    val mimeType: String = "image/jpeg"
)
