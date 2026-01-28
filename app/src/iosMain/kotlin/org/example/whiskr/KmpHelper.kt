package org.example.whiskr

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import org.example.whiskr.di.createIosModule
import org.example.whiskr.share.IosImageShareManager

object KmpHelper {
    private val lifecycle = LifecycleRegistry()
    private val databaseFactory = IosDatabaseFactory()
    private val shareService = IosShareService()
    private val imageShareManager = IosImageShareManager()
    private val appComponent = createIosModule(databaseFactory, shareService, imageShareManager)

    val root =
        appComponent.rootComponentFactory(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            deepLink = null
        )

    fun resume() {
        lifecycle.resume()
    }

    fun pause() {
        lifecycle.pause()
    }

    fun stop() {
        lifecycle.stop()
    }

    fun destroy() {
        lifecycle.destroy()
    }

    fun handleDeepLink(url: String) {
        root.onDeepLink(url)
    }
}
