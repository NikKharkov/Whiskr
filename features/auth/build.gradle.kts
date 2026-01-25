import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.features.auth"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kmpauth.google)
            implementation(libs.kmpauth.firebase)
            implementation(libs.kmpauth.uihelper)
        }
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.bom))
        }
    }
}