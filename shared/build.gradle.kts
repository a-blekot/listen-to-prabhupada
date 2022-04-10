plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-core:${findProperty("version.ktor")}")
                implementation("io.ktor:ktor-client-content-negotiation:${findProperty("version.ktor")}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:${findProperty("version.ktor")}")
                implementation("io.ktor:ktor-client-logging:${findProperty("version.ktor")}")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${findProperty("version.kotlinx.coroutines")}")
                //Logger
                implementation("io.github.aakira:napier:${findProperty("version.napier")}")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${findProperty("version.kotlinx.serialization")}")
                //Key-Value storage
                implementation("com.russhwolf:multiplatform-settings-no-arg:0.8.1")
                //Data Base
                implementation("com.squareup.sqldelight:runtime:${findProperty("version.sql_delight")}")
                implementation("com.squareup.sqldelight:coroutines-extensions:${findProperty("version.sql_delight")}")


                // File IO - okio
                implementation("com.squareup.okio:okio:${findProperty("version.okio")}")

                // Stately
                implementation("co.touchlab:stately-common:${findProperty("version.stately")}")
                implementation("co.touchlab:stately-concurrency:${findProperty("version.stately")}")
                implementation("co.touchlab:stately-isolate:${findProperty("version.stately")}")
                implementation("co.touchlab:stately-iso-collections:${findProperty("version.stately")}")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-okhttp:${findProperty("version.ktor")}")
                implementation("com.squareup.sqldelight:android-driver:${findProperty("version.sql_delight")}")
                //Logger
//                implementation("io.github.aakira:napier-android:${findProperty("version.napier")}")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                //Network
//                implementation("io.ktor:ktor-client-ios:${findProperty("version.ktor")}")
                implementation("io.ktor:ktor-client-darwin:${findProperty("version.ktor")}")
                implementation("com.squareup.sqldelight:native-driver:${findProperty("version.sql_delight")}")
                //Logger
//                implementation("io.github.aakira:napier-ios:${findProperty("version.napier")}")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.anadi.prabhupadalectures.data"
        dialect = "sqlite:3.25"
    }
}