import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.features.ai_studio"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.peekaboo.ui)
            implementation(libs.peekaboo.image.picker)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.filekit.compose)
        }
    }
}