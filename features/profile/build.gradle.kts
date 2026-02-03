import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.features.profile"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.user)
            implementation(projects.features.notifications.api)
            implementation(projects.features.posts.api)
        }
    }
}