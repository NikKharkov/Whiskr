package convention

import convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class WhiskrRoomPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("com.google.devtools.ksp")
        pluginManager.apply("androidx.room")

        dependencies {
            add("commonMainImplementation", libs.findLibrary("room-runtime").get())
            add("commonMainImplementation", libs.findLibrary("sqlite-bundled").get())

            val roomCompiler = libs.findLibrary("room-compiler").get()
            add("kspAndroid", roomCompiler)
            add("kspIosSimulatorArm64", roomCompiler)
            add("kspIosArm64", roomCompiler)
        }
    }
}