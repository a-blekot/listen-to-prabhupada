enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Prabhupada_Lectures"
include(
    ":android",
    ":common:database",
    ":common:downloads-api",
    ":common:downloads-impl",
    ":common:favorites-api",
    ":common:favorites-impl",
    ":common:feature-downloads-api",
    ":common:feature-downloads-impl",
    ":common:feature-favorites-api",
    ":common:feature-favorites-impl",
    ":common:feature-results-api",
    ":common:feature-results-impl",
    ":common:filters",
    ":common:lectures-api",
    ":common:lectures-impl",
    ":common:network-api",
    ":common:network-impl",
    ":common:player-api",
    ":common:player-impl",
    ":common:root",
    ":common:settings",
    ":common:utils"
)
