plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.posts"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.user)

            implementation(libs.calf.file.picker)
            implementation(libs.calf.file.picker.coil)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.media.player)
        }
    }
}