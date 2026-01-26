package convention

import convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class WhiskrComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        val composeVersion = libs.findVersion("composeMultiplatform").get()

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                named("commonMain") {
                    dependencies {
                        implementation("org.jetbrains.compose.runtime:runtime:$composeVersion")
                        implementation("org.jetbrains.compose.foundation:foundation:$composeVersion")
                        implementation("org.jetbrains.compose.material3:material3:1.9.0")
                        implementation("org.jetbrains.compose.ui:ui:$composeVersion")
                        implementation("org.jetbrains.compose.components:components-resources:$composeVersion")
                        implementation("org.jetbrains.compose.ui:ui-tooling-preview:$composeVersion")
                    }
                }

                named("androidMain") {
                    dependencies {
                        implementation("org.jetbrains.compose.ui:ui-tooling-preview:$composeVersion")
                        implementation("org.jetbrains.compose.ui:ui-tooling:$composeVersion")
                    }
                }
            }
        }
    }
}