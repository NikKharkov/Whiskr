package convention

import com.android.build.gradle.AppExtension
import convention.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class WhiskrAndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")

        val compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        val minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
        val targetSdk = libs.findVersion("android-targetSdk").get().requiredVersion.toInt()

        extensions.configure<AppExtension> {
            compileSdkVersion(compileSdk)
            defaultConfig {
                minSdkVersion(minSdk)
                targetSdkVersion(targetSdk)
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }
}