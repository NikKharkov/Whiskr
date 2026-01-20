package org.example.whiskr.util

fun String.toCloudStorageUrl(): String {
    return this.replace("localhost", "192.168.1.60")
}

//fun String.toCloudStorageUrl(): String {
//    if (this.contains("51.20.31.140")) return this
//
//    return this.replace("http://minio:9000", "http://51.20.31.140:9000")
//}