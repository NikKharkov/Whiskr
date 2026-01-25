plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.auth"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kmpauth.google)
            implementation(libs.kmpauth.firebase)
            implementation(libs.kmpauth.uihelper)
        }
    }
}