plugins {
    id("whiskr.app")
    kotlin("native.cocoapods")
}

dependencies {
    debugImplementation(compose.uiTooling)
    commonMainImplementation(projects.core.network)
    commonMainImplementation(projects.core.storage)
    commonMainImplementation(projects.core.common)
    commonMainImplementation(projects.core.ui)
    commonMainImplementation(projects.core.user)
    commonMainImplementation(libs.kvault)
    commonMainImplementation(projects.features.auth)
    commonMainImplementation(projects.features.registration)
    commonMainImplementation(projects.features.home)
    commonMainImplementation(projects.flows.auth)
    commonMainImplementation(projects.flows.main)
    commonMainImplementation(libs.multiplatform.settings)
    androidMainImplementation(libs.androidx.activity.compose)
}

android {
    namespace = "org.example.whiskr"
    defaultConfig {
        applicationId = "org.example.whiskr"
        versionCode = 1
        versionName = "1.0"
    }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

kotlin {
    cocoapods {
        version = "1.0"
        summary = "Whiskr Mobile App"
        homepage = "https://whiskr.app"

        ios.deploymentTarget = "15.0"

        framework {
            baseName = "ComposeApp"
            isStatic = true
        }

        podfile = project.file("../iosApp/Podfile")

        pod("FirebaseCore") { version = "10.22.0" }
        pod("FirebaseAuth") { version = "10.22.0" }
        pod("FirebaseMessaging") { version = "10.22.0" }
        pod("GoogleSignIn") { version = "7.1.0" }
        pod("FBSDKCoreKit") { version = "17.0.0" }
        pod("FBSDKLoginKit") { version = "17.0.0" }
    }
}
