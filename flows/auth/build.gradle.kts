plugins {
    id("whiskr.feature")
}

android {
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