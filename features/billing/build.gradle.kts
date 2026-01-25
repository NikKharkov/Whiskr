plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.billing"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.stripe.android)
        }
    }
}