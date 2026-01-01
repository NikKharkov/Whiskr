plugins {
    id("whiskr.feature")
}

android {
    namespace = "org.example.whiskr.main"
}

dependencies {
    commonMainImplementation(projects.features.main)
}