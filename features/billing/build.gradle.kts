import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.features.billing"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.stripe.android)
        }
    }
}