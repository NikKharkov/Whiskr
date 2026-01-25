package convention

import convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class WhiskrFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("whiskr.kmp")
        pluginManager.apply("whiskr.android.library")
        pluginManager.apply("whiskr.compose")
        pluginManager.apply("whiskr.navigation")
        pluginManager.apply("whiskr.di")
        pluginManager.apply("whiskr.network")
        pluginManager.apply("whiskr.testing")

        dependencies {
            add("commonMainImplementation",libs.findLibrary("kermit").get())
            add("commonMainImplementation",project(":core:common"))
            add("commonMainImplementation",project(":core:ui"))
            add("commonMainImplementation",project(":core:network"))
        }
    }
}