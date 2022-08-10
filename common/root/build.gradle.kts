plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("kotlin-parcelize")
}

kotlin {

//    if (providers.gradleProperty("include_ios").get().toBoolean()) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries {
                framework {
                    baseName = "Prabhupada"
                    linkerOpts.add("-lsqlite3")
                    export(projects.common.database)
                    export(projects.common.features.featureResultsApi)
                    export(projects.common.features.featureFavoritesApi)
                    export(projects.common.features.featureDownloadsApi)
                    export(projects.common.favoritesApi)
                    export(projects.common.downloadsApi)
                    export(projects.common.filtersApi)
                    export(projects.common.filtersImpl)
                    export(projects.common.settingsApi)
                    export(projects.common.settingsImpl)
                    export(projects.common.resultsApi)
                    export(projects.common.networkApi)
                    export(projects.common.networkImpl)
                    export(projects.common.playerApi)
                    export(projects.common.playerImpl)
                    export(projects.common.utils)
                    export(libs.decompose.decompose)
                    export(libs.mvikotlin.main)
                    export(libs.essenty.lifecycle)
                }
            }
        }
//    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.utils)
                implementation(projects.common.database)
                implementation(projects.common.networkApi)
                implementation(projects.common.playerApi)
                implementation(projects.common.features.featureResultsApi)
                implementation(projects.common.features.featureResultsImpl)
                implementation(projects.common.features.featureFavoritesApi)
                implementation(projects.common.features.featureFavoritesImpl)
                implementation(projects.common.features.featureDownloadsApi)
                implementation(projects.common.features.featureDownloadsImpl)
                implementation(projects.common.filtersApi)
                implementation(projects.common.filtersImpl)
                implementation(projects.common.settingsApi)
                implementation(projects.common.settingsImpl)
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.decompose.decompose)
            }
        }
    }

    sourceSets {
        findByName("iosMain")?.run  {
            dependencies {
                api(projects.common.database)
                api(projects.common.favoritesApi)
                api(projects.common.downloadsApi)
                api(projects.common.features.featureResultsApi)
                api(projects.common.features.featureFavoritesApi)
                api(projects.common.features.featureDownloadsApi)
                api(projects.common.filtersApi)
                api(projects.common.filtersImpl)
                api(projects.common.settingsApi)
                api(projects.common.settingsImpl)
                api(projects.common.resultsApi)
                api(projects.common.networkApi)
                api(projects.common.networkImpl)
                api(projects.common.playerApi)
                api(projects.common.playerImpl)
                api(projects.common.utils)
                api(libs.decompose.decompose)
                api(libs.mvikotlin.main)
                api(libs.essenty.lifecycle)
            }
        }
    }
}
