import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.di")
    id("whiskr.network")
    id("whiskr.navigation")
    id("whiskr.compose")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.core.user"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(projects.core.storage)
            implementation(libs.kermit)
        }
    }
}