plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.prabhupadalectures.common.database"
        dialect = "sqlite:3.25"
    }
}

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
