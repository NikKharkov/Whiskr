import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.android.library")
    id("whiskr.kmp")
    id("whiskr.di")
    id("whiskr.room")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.core.storage"
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(libs.kermit)
            implementation(libs.multiplatform.settings)
        }
    }
}