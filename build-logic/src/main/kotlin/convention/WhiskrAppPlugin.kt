package convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class WhiskrAppPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("whiskr.kmp")
        pluginManager.apply("whiskr.android.application")
        pluginManager.apply("org.jetbrains.compose.hot-reload")
        pluginManager.apply("whiskr.compose")
        pluginManager.apply("whiskr.navigation")
        pluginManager.apply("whiskr.di")
        pluginManager.apply("whiskr.network")
        pluginManager.apply("whiskr.testing")
        pluginManager.apply("com.google.gms.google-services")
        pluginManager.apply("org.jetbrains.kotlin.native.cocoapods")
    }
}