import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("whiskr.app")
    kotlin("native.cocoapods")
}

configure<ApplicationExtension> {
    namespace = "org.example.whiskr"

    defaultConfig {
        applicationId = "org.example.whiskr"
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
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
            export(projects.features.billing)
            export(libs.kmp.notifier)
        }

        podfile = project.file("../iosApp/Podfile")

        pod("FirebaseCore") { version = "10.22.0" }
        pod("FirebaseAuth") { version = "10.22.0" }
        pod("FirebaseMessaging") { version = "10.22.0" }
        pod("GoogleSignIn") { version = "7.1.0" }
        pod("FBSDKCoreKit") { version = "17.0.0" }
        pod("FBSDKLoginKit") { version = "17.0.0" }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.flows.main)
            implementation(projects.flows.auth)
            api(projects.features.billing)
            api(libs.kmp.notifier)

            implementation(projects.core.common)
            implementation(projects.core.network)
            implementation(projects.core.storage)
            implementation(projects.core.ui)
            implementation(projects.core.user)

            implementation(libs.kvault)
            implementation(libs.multiplatform.settings)
            implementation(libs.filekit.compose)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}