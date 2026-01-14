plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.network")
    id("whiskr.di")
}

android {
    namespace = "org.example.whiskr.core.network"
}

dependencies {
    commonMainImplementation(projects.core.common)
    commonMainImplementation(libs.kermit)
    commonMainImplementation(libs.kvault)
    commonMainImplementation(libs.ktor.client.auth)
    commonMainImplementation(libs.kotlinx.datetime)
}