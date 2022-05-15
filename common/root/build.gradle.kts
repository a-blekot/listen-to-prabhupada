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
                baseName = "MyApp"
                linkerOpts.add("-lsqlite3")
                export(project(":common:database"))
                export(project(":common:feature-results-api"))
                export(project(":common:filters"))
                export(project(":common:lectures-api"))
                export(project(":common:network-api"))
                export(project(":common:network-impl"))
                export(project(":common:player-api"))
                export(project(":common:player-impl"))
                export(project(":common:utils"))
                export(libs.decompose.decompose)
                export(libs.mvikotlin.main)
                export(libs.essenty.lifecycle)
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:utils"))
                implementation(project(":common:database"))
                implementation(project(":common:network-api"))
                implementation(project(":common:player-api"))
                implementation(project(":common:feature-results-api"))
                implementation(project(":common:feature-results-impl"))
                implementation(project(":common:filters"))
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.decompose.decompose)
            }
        }
    }

    sourceSets {
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting {
            dependencies {
                api(project(":common:database"))
                api(project(":common:feature-results-api"))
                api(project(":common:filters"))
                api(project(":common:lectures-api"))
                api(project(":common:network-api"))
                api(project(":common:network-impl"))
                api(project(":common:player-api"))
                api(project(":common:player-impl"))
                api(project(":common:utils"))
                api(libs.decompose.decompose)
                api(libs.mvikotlin.main)
                api(libs.essenty.lifecycle)
            }
        }

        val iosMain by getting {
            dependencies {
                api(project(":common:database"))
                api(project(":common:feature-results-api"))
                api(project(":common:filters"))
                api(project(":common:lectures-api"))
                api(project(":common:network-api"))
                api(project(":common:network-impl"))
                api(project(":common:player-api"))
                api(project(":common:player-impl"))
                api(project(":common:utils"))
                api(libs.decompose.decompose)
                api(libs.mvikotlin.main)
                api(libs.essenty.lifecycle)
            }

            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
        }
    }
}
