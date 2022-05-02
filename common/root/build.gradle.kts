plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("kotlin-parcelize")
}

kotlin {
    ios {
        binaries {
            framework {
                baseName = "Prabhupada"
                linkerOpts.add("-lsqlite3")
                export(project(":common:database"))
                export(project(":common:main"))
                export(project(":common:edit"))
                export(libs.decompose.decompose)
                export(libs.mvikotlin.main)
                export(libs.essenty.lifecycle)
            }
        }
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(project(":common:utils"))
                implementation(project(":common:database"))
                implementation(project(":common:main"))
                implementation(project(":common:edit"))
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.decompose.decompose)
            }
        }
    }

    sourceSets {
        named("iosMain") {
            dependencies {
                api(project(":common:database"))
                api(project(":common:main"))
                api(project(":common:edit"))
                api(libs.decompose.decompose)
                api(libs.mvikotlin.main)
            }
        }
    }
}
