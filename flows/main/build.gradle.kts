plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.main"
}

dependencies {
    commonMainImplementation(projects.core.user)
    commonMainImplementation(projects.features.home)
    commonMainImplementation(libs.coil.compose)
    commonMainImplementation(libs.coil.network)
}