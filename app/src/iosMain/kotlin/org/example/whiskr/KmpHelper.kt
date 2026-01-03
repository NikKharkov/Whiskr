package org.example.whiskr

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import com.russhwolf.settings.NSUserDefaultsSettings
import org.example.whiskr.di.createIosComponent
import platform.Foundation.NSUserDefaults

object KmpHelper {
    private val lifecycle = LifecycleRegistry()
    private val databaseFactory = IosDatabaseFactory()
    private val settings = NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)

    private val appComponent = createIosComponent(databaseFactory, settings)

    val root =
        appComponent.rootComponentFactory(
            DefaultComponentContext(lifecycle = lifecycle)
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
}
