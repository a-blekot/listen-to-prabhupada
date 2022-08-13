plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("kotlin-parcelize")
}

kotlin {

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries {
            framework {
                baseName = "Prabhupada"
                linkerOpts.add("-lsqlite3")
                export(projects.common.data)
                export(projects.common.database)
                export(projects.common.downloadsApi)
                export(projects.common.favoritesApi)
                export(projects.common.filtersApi)
                export(projects.common.networkApi)
                export(projects.common.networkImpl)
                export(projects.common.playerApi)
                export(projects.common.playerImpl)
                export(projects.common.resultsApi)
                export(projects.common.settingsApi)
                export(projects.common.utils)

                export(libs.decompose.decompose)
                export(libs.essenty.lifecycle)
                export(libs.mvikotlin.main)
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.data)
                implementation(projects.common.database)
                implementation(projects.common.downloadsApi)
                implementation(projects.common.downloadsImpl)
                implementation(projects.common.favoritesApi)
                implementation(projects.common.favoritesImpl)
                implementation(projects.common.filtersApi)
                implementation(projects.common.filtersImpl)
                implementation(projects.common.networkApi)
                implementation(projects.common.playerApi)
                implementation(projects.common.playerImpl)
                implementation(projects.common.resultsApi)
                implementation(projects.common.resultsImpl)
                implementation(projects.common.settingsApi)
                implementation(projects.common.settingsImpl)
                implementation(projects.common.utils)

                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.decompose.decompose)
            }
        }

        iosMain {
            dependencies {
                api(projects.common.data)
                api(projects.common.database)
                api(projects.common.downloadsApi)
                api(projects.common.favoritesApi)
                api(projects.common.filtersApi)
                api(projects.common.networkApi)
                api(projects.common.networkImpl)
                api(projects.common.playerApi)
                api(projects.common.playerImpl)
                api(projects.common.resultsApi)
                api(projects.common.settingsApi)
                api(projects.common.utils)

                api(libs.decompose.decompose)
                api(libs.essenty.lifecycle)
                api(libs.mvikotlin.main)
            }
        }
    }
}
