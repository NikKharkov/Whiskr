plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.di")
    id("whiskr.navigation")
}

android {
    namespace = "org.example.whiskr.core.common"
}

dependencies {
    commonMainImplementation(libs.kotlinx.coroutines.core)
}