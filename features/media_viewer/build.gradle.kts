import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.features.mediaviewer"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.posts.api)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.zoomable)
            implementation(libs.filekit.compose)
            implementation(libs.media.player)
        }
    }
}