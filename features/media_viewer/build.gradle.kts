plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.mediaviewer"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.zoomable)
            implementation(libs.filekit.compose)
            implementation(libs.media.player)
        }
    }
}