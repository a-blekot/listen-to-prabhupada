plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("kotlin-parcelize")
}

kotlin {

    if (providers.gradleProperty("include_ios").get().toBoolean()) {
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
                    export(projects.common.featureResultsApi)
                    export(projects.common.featureFavoritesApi)
                    export(projects.common.featureDownloadsApi)
                    export(projects.common.filters)
                    export(projects.common.lecturesApi)
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
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.utils)
                implementation(projects.common.database)
                implementation(projects.common.networkApi)
                implementation(projects.common.playerApi)
                implementation(projects.common.featureResultsApi)
                implementation(projects.common.featureResultsImpl)
                implementation(projects.common.featureFavoritesApi)
                implementation(projects.common.featureFavoritesImpl)
                implementation(projects.common.featureDownloadsApi)
                implementation(projects.common.featureDownloadsImpl)
                implementation(projects.common.filters)
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.decompose.decompose)
            }
        }
    }

    sourceSets {
        findByName("iosMain")?.run  {
            dependencies {
                api(projects.common.database)
                api(projects.common.featureResultsApi)
                api(projects.common.featureFavoritesApi)
                api(projects.common.filters)
                api(projects.common.lecturesApi)
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
