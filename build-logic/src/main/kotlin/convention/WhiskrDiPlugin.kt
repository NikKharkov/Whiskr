package convention

import convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class WhiskrDiPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.named("commonMain") {
                dependencies {
                    implementation(libs.findLibrary("kotlin-inject-runtime").get())
                }
            }
        }

        dependencies {
            val compiler = libs.findLibrary("kotlin-inject-compiler").get()

            add("kspCommonMainMetadata", compiler)
            add("kspAndroid", compiler)
            add("kspIosArm64", compiler)
            add("kspIosSimulatorArm64", compiler)
        }
    }
}