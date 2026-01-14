package org.example.whiskr.util

fun String.toCloudStorageUrl(): String {
    return this.replace("localhost", "192.168.1.60")
}