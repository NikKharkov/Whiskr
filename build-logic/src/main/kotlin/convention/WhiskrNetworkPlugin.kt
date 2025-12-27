package convention

import convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class WhiskrNetworkPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        pluginManager.apply("de.jensklingenberg.ktorfit")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                named("commonMain") {
                    dependencies {
                        implementation(libs.findLibrary("ktorfit-lib").get())
                        implementation(libs.findLibrary("ktor-client-core").get())
                        implementation(libs.findLibrary("ktor-client-content-negotiation").get())
                        implementation(libs.findLibrary("ktor-serialization-kotlinx-json").get())
                        implementation(libs.findLibrary("ktor-client-logging").get())
                        implementation(libs.findLibrary("kotlinx-serialization-json").get())
                    }
                }

                named("androidMain") { dependencies { implementation(libs.findLibrary("ktor-client-okhttp").get()) } }
                named("iosMain") { dependencies { implementation(libs.findLibrary("ktor-client-darwin").get()) } }
            }
        }
    }
}