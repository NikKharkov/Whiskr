import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.features.home"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.user)
            implementation(projects.features.posts.api)

            implementation(libs.calf.file.picker)
        }
    }
}