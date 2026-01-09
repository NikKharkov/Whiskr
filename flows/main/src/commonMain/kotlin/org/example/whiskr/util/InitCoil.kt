package org.example.whiskr.util

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import com.mohamedrejeb.calf.picker.coil.KmpFileFetcher

@Composable
fun InitCoil() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KmpFileFetcher.Factory())
            }
            .build()
    }
}