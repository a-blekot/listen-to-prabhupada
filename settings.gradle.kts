rootProject.name = "Prabhupada_Lectures"

includeBuild("build-logic")
include(
    ":common:utils",
    ":common:database",
    ":common:network-api",
    ":common:network-impl",
    ":common:main",
    ":common:edit",
    ":common:lectures",
    ":common:root",
    ":android"
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

//    resolutionStrategy {
//        eachPlugin {
//            val pluginId = requested.id.id
//            if (pluginId.startsWith("org.jetbrains.kotlin")) {
//                useVersion("1.6.20")
//            } else if (pluginId.startsWith("com.android.tools.build")) {
////                useModule("com.android.tools.build:gradle:7.2.0-beta04")
//                useModule("com.android.tools.build:gradle:7.3.0-alpha09")
//            }
//        }
//    }
}
