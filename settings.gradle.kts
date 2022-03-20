pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Prabhupada_Lectures"
include(":androidApp")
include(":shared")
include(":common:coroutines")
include(":common:navigation")
include(":common:viewmodel")
