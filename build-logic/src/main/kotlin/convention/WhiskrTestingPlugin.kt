package convention

import convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class WhiskrTestingPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("dev.mokkery")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.named("commonTest") {
                dependencies {
                    implementation(libs.findLibrary("kotlin-test").get())
                    implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                    implementation(libs.findLibrary("turbine").get())
                    implementation(libs.findLibrary("mokkery").get())
                    implementation(libs.findLibrary("kotest-assertions").get())
                }
            }
        }
    }
}