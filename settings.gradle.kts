dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Prabhupada_Lectures"
include(":androidApp")
include(":shared")
enableFeaturePreview("VERSION_CATALOGS")
