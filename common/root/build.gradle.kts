plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("kotlin-parcelize")
}

kotlin {

//    targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>("iosX64").compilations.forEach {
//        it.kotlinOptions.freeCompilerArgs += arrayOf("-linker-options", "-lsqlite3")
//    }

    ios {
        binaries {
            framework {
                baseName = "Prabhupada"
                linkerOpts.add("-lsqlite3")
                export(project(":common:database"))
                export(project(":common:lectures-api"))
                export(project(":common:filters"))
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
        iosMain {
            dependencies {
                api(project(":common:database"))
                api(project(":common:lectures-api"))
                api(project(":common:filters"))
                api(libs.decompose.decompose)
                api(libs.mvikotlin.main)
            }
        }
    }
}
