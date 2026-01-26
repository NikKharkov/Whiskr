import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.di")
    id("whiskr.navigation")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.core.common"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}