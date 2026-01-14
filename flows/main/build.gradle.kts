plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.main"
}

dependencies {
    commonMainImplementation(projects.core.user)
    commonMainImplementation(projects.core.storage)
    commonMainImplementation(projects.features.posts)
    commonMainImplementation(projects.features.mediaViewer)
    commonMainImplementation(libs.coil.compose)
    commonMainImplementation(libs.coil.network)
    commonMainImplementation(libs.calf.file.picker.coil)
}