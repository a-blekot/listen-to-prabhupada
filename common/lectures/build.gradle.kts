plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("kotlin-parcelize")
}

kotlin {
    ios() {
        binaries {
            framework {
                baseName = "Prabhupad"
                linkerOpts.add("-lsqlite3")
                export(libs.decompose.decompose)
                export(libs.mvikotlin.main)
                export(libs.essenty.lifecycle)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common:database"))
                implementation(project(":common:network-api"))
                implementation(project(":common:utils"))

                implementation(libs.decompose.decompose)
                implementation(libs.bundles.mvikotlin.bndl)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.core)
                implementation(libs.napier)
                implementation(libs.settings)
                implementation(libs.okio)
                implementation(libs.bundles.stately.bndl)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.decompose.extCompose)
            }
        }
    }
}
