plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.registration"
}

dependencies {
    commonMainImplementation(libs.peekaboo.ui)
    commonMainImplementation(libs.peekaboo.image.picker)
    commonMainImplementation(libs.coil.compose)
    commonMainImplementation(libs.coil.network)
}
