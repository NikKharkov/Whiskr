plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.di")
    id("whiskr.navigation")
}

android {
    namespace = "org.example.whiskr.core.common"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}