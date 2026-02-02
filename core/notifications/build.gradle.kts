import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.core.notifications"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kmp.notifier)
        }
    }
}