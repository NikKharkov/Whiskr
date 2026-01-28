import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.main"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.posts)
            api(projects.features.mediaViewer)
            api(projects.features.billing)
            api(projects.features.aiStudio)

            implementation(projects.core.user)
            implementation(projects.core.storage)

            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.calf.file.picker.coil)
        }
    }
}