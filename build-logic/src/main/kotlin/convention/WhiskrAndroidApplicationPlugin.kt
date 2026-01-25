package convention

import com.android.build.api.dsl.ApplicationExtension
import convention.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class WhiskrAndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
        }

        val compileSdkVer = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        val minSdkVer = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
        val targetSdkVer = libs.findVersion("android-targetSdk").get().requiredVersion.toInt()

        extensions.configure<ApplicationExtension> {
            compileSdk = compileSdkVer

            defaultConfig {
                minSdk = minSdkVer
                targetSdk = targetSdkVer

                versionCode = 1
                versionName = "1.0"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }
}