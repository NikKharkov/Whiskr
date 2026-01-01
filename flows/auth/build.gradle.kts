plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.auth"
}

dependencies {
    commonMainImplementation(projects.features.auth)
    commonMainImplementation(projects.features.registration)
}