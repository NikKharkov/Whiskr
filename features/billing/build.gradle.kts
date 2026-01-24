plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.billing"
}

dependencies {
    commonMainImplementation(libs.kotlinx.datetime)

    androidMainApi(libs.stripe.android)
}