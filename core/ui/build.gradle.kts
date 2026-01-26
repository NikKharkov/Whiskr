import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.compose")
}

configure<LibraryExtension> {
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