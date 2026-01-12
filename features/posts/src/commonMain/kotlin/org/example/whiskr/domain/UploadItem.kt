package org.example.whiskr.domain

data class UploadItem(
    val bytes: ByteArray? = null,
    val filePath: String? = null,
    val filename: String,
    val mimeType: String
)
