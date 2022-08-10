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
    ":common:features:feature-downloads-api",
    ":common:features:feature-downloads-impl",
    ":common:features:feature-favorites-api",
    ":common:features:feature-favorites-impl",
    ":common:features:feature-results-api",
    ":common:features:feature-results-impl"
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
