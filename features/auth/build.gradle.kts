plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.features.auth"
}

dependencies {
    commonMainImplementation(libs.kmpauth.google)
    commonMainImplementation(libs.kmpauth.firebase)
    commonMainImplementation(libs.kmpauth.uihelper)
}
