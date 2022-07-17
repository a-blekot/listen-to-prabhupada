plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.listentoprabhupada.common.database"
        dialect = "sqlite:3.25"
    }
}

//Please wait while Kotlin/Native compiler 1.7.0 is being installed.
//Download https://download.jetbrains.com/kotlin/native/builds/releases/1.7.0/macos-aarch64/kotlin-native-prebuilt-macos-aarch64-1.7.0.tar.gz


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.sqlDelight.runtime)
                implementation(libs.sqlDelight.coroutines.extensions)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.sqlDelight.android.driver)
                implementation(libs.sqlDelight.sqlite.driver)
            }
        }

        findByName("iosMain")?.run {
            dependencies {
                implementation(libs.sqlDelight.native.driver)
            }
        }
    }
}
