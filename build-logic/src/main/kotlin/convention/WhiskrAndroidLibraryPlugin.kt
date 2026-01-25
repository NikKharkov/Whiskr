package convention

import com.android.build.api.dsl.LibraryExtension
import convention.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class WhiskrAndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
        }

        val compileSdkVer = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        val minSdkVer = libs.findVersion("android-minSdk").get().requiredVersion.toInt()

        extensions.configure<LibraryExtension> {
            compileSdk = compileSdkVer

            defaultConfig {
                minSdk = minSdkVer
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }
}