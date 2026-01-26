import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.network")
    id("whiskr.di")
    id("whiskr.navigation")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.core.network"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(libs.kotlinx.datetime)
            implementation(libs.kermit)
            implementation(libs.kvault)
            implementation(libs.ktor.client.auth)
        }
    }
}