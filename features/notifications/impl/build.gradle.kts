import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.feature.notifications.impl"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.notifications.api)
            implementation(libs.kmp.notifier)
        }
    }
}