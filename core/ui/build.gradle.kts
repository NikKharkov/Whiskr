plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.compose")
}

android {
    namespace = "org.example.whiskr.core.ui"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(libs.coil.compose)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core)
        }
    }
}