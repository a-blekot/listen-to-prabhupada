rootProject.name = "Prabhupada_Lectures"

includeBuild("build-logic")
include(
    ":android",
    ":common:database",
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

enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        google()
        mavenCentral()
    }
}
