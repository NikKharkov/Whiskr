package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class WhiskrKmpPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        extensions.configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()
            iosArm64()
            iosSimulatorArm64()
        }

        val configureAndroid = {
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }
                }
            }
        }

        pluginManager.withPlugin("com.android.application") { configureAndroid() }
        pluginManager.withPlugin("com.android.library") { configureAndroid() }
    }
}