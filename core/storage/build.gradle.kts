plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.di")
    id("whiskr.network")
    id("whiskr.navigation")
    id("whiskr.room")
}


android {
    namespace = "org.example.whiskr.core.storage"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    commonMainImplementation(libs.kermit)
    commonMainImplementation(projects.core.common)
    commonMainImplementation(libs.multiplatform.settings)
}