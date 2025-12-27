package convention

import com.android.build.api.dsl.LibraryExtension
import convention.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class WhiskrAndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")

        val compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        val minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()

        extensions.configure<LibraryExtension> {
            this.compileSdk = compileSdk
            defaultConfig { this.minSdk = minSdk }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }
}