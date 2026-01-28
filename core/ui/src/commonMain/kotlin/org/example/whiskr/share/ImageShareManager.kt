package org.example.whiskr.share

interface ImageShareManager {
    suspend fun shareImages(images: List<ByteArray>): Result<Unit>
}