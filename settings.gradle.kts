rootProject.name = "Whiskr"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":app")

include(":flows:auth")
include(":flows:main")

include(":features:auth")
include(":features:registration")
include(":features:home")
include(":features:posts:api")
include(":features:posts:impl")
include(":features:media_viewer")
include(":features:billing")
include(":features:ai_studio")
include(":features:profile")

include(":core:common")
include(":core:network")
include(":core:storage")
include(":core:user")
include(":core:ui")