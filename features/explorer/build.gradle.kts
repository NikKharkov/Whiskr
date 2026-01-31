import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.features.explore"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.posts.api)
            implementation(libs.rssparser)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.compose.webview.multiplatform)
        }
    }
}