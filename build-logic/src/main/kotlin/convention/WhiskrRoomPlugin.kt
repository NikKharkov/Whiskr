package convention

import convention.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class WhiskrRoomPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("com.google.devtools.ksp")
        pluginManager.apply("androidx.room")

        dependencies.add("commonMainImplementation", libs.findLibrary("room-runtime").get())
        dependencies.add("kspCommonMainMetadata", libs.findLibrary("room-compiler").get())
        dependencies.add("commonMainImplementation", libs.findLibrary("sqlite-bundled").get())
    }
}