package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class WhiskrKmpPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()

            androidTarget()
            iosArm64()
            iosSimulatorArm64()
        }
    }
}