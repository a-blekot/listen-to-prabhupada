pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "listen-to-prabhupada"
include(
    ":android",
    ":android-ui",
    ":common:data",
    ":common:database",
    ":common:downloads-api",
    ":common:downloads-impl",
    ":common:favorites-api",
    ":common:favorites-impl",
    ":common:filters-api",
    ":common:filters-impl",
    ":common:results-api",
    ":common:results-impl",
    ":common:network-api",
    ":common:network-impl",
    ":common:player-api",
    ":common:player-impl",
    ":common:root",
    ":common:settings",
    ":common:settings-api",
    ":common:settings-impl",
    ":common:utils",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
