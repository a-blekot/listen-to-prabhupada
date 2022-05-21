plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:network-api"))
                implementation(project(":common:utils"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.bundles.ktor.common.bndl)
                implementation(libs.napier)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        findByName("iosMain")?.run {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}