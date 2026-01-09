package org.example.whiskr.domain

import com.mohamedrejeb.calf.io.KmpFile


interface MediaProcessingService {
    suspend fun processImage(file: KmpFile): ProcessedImage
    suspend fun processVideo(file: KmpFile): ProcessedVideo
    fun readFile(path: String): ByteArray
}