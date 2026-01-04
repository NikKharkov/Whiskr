plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.compose")
}

android {
    namespace = "org.example.whiskr.core.ui"
}

dependencies {
    commonMainImplementation(libs.coil.compose)
}