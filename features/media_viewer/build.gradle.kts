plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.mediaviewer"
}

dependencies {
    commonMainImplementation(libs.kotlinx.datetime)
    commonMainImplementation(libs.coil.compose)
    commonMainImplementation(libs.coil.network)
    commonMainImplementation(libs.media.player)
    commonMainImplementation(libs.zoomable)
    commonMainImplementation(libs.filekit.compose)
    commonMainImplementation(libs.media.player)
}
