import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension>  {
    namespace = "org.example.whiskr.auth"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.auth)
            api(projects.features.registration)
        }
    }
}