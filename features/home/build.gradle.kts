plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.home"
}

dependencies {
    androidMainImplementation(libs.light.compressor)
    commonMainImplementation(libs.kotlinx.datetime)
    commonMainImplementation(libs.calf.file.picker)
    commonMainImplementation(libs.calf.file.picker.coil)
    commonMainImplementation(libs.coil.compose)
    commonMainImplementation(libs.coil.network)
    commonMainImplementation(projects.core.user)
}
