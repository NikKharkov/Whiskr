plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.billing"
}

dependencies {
    commonMainImplementation(libs.kotlinx.datetime)

    androidMainImplementation(libs.stripe.android)
}