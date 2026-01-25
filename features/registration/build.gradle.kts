plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.registration"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.peekaboo.ui)
            implementation(libs.peekaboo.image.picker)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
        }
    }
}