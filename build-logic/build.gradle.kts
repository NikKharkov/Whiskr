plugins {
    `kotlin-dsl`
}

group = "com.whiskr.buildlogic"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("whiskrKmp") {
            id = "whiskr.kmp"
            implementationClass = "convention.WhiskrKmpPlugin"
        }
        register("whiskrAndroidLibrary") {
            id = "whiskr.android.library"
            implementationClass = "convention.WhiskrAndroidLibraryPlugin"
        }
        register("whiskrAndroidApplication") {
            id = "whiskr.android.application"
            implementationClass = "convention.WhiskrAndroidApplicationPlugin"
        }
        register("whiskrCompose") {
            id = "whiskr.compose"
            implementationClass = "convention.WhiskrComposePlugin"
        }
        register("whiskrNavigation") {
            id = "whiskr.navigation"
            implementationClass = "convention.WhiskrNavigationPlugin"
        }
        register("whiskrDi") {
            id = "whiskr.di"
            implementationClass = "convention.WhiskrDiPlugin"
        }
        register("whiskrNetwork") {
            id = "whiskr.network"
            implementationClass = "convention.WhiskrNetworkPlugin"
        }
        register("whiskrRoom") {
            id = "whiskr.room"
            implementationClass = "convention.WhiskrRoomPlugin"
        }
        register("whiskrTesting") {
            id = "whiskr.testing"
            implementationClass = "convention.WhiskrTestingPlugin"
        }
        register("whiskrFeature") {
            id = "whiskr.feature"
            implementationClass = "convention.WhiskrFeaturePlugin"
        }
        register("whiskrApp") {
            id = "whiskr.app"
            implementationClass = "convention.WhiskrAppPlugin"
        }
    }
}