plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.compose")
}

android {
    namespace = "org.example.whiskr.core.ui"
}

dependencies {
    androidMainImplementation(libs.androidx.core)
    commonMainImplementation(projects.core.common)
    commonMainImplementation(libs.coil.compose)
}