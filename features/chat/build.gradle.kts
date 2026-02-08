import com.android.build.api.dsl.LibraryExtension

plugins {
    id("whiskr.feature")
}

configure<LibraryExtension> {
    namespace = "org.example.whiskr.feature.chat"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.posts.api)
            implementation(projects.core.user)
            implementation(libs.krossbow.stomp.core)
            implementation(libs.krossbow.websocket.builtin)
            implementation(libs.krossbow.stomp.kxserialization.json)
            implementation(libs.krossbow.websocket.ktor)

            implementation(libs.calf.file.picker)
            implementation(libs.calf.file.picker.coil)
            implementation(libs.media.player)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
        }
    }
}