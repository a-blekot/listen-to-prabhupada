plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.networkApi)
                implementation(projects.common.utils)
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