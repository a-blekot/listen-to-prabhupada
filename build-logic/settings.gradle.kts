enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    repositories {
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "build-logic"
include("convention-plugins")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

