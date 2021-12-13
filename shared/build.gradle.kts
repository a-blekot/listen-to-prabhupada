plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

kotlin {
    android()
    
    listOf(
        iosX64(),
        iosArm64(),
        //iosSimulatorArm64() sure all ios dependencies support this target
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
                implementation("io.ktor:ktor-client-json:${findProperty("version.ktor")}")
                implementation("io.ktor:ktor-client-serialization:${findProperty("version.ktor")}")
                implementation("io.ktor:ktor-client-logging:${findProperty("version.ktor")}")
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${findProperty("version.kotlinx.coroutines")}")
                //Logger
                implementation("io.github.aakira:napier:2.1.0")
                //JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${findProperty("version.kotlinx.serialization")}")
                //Key-Value storage
                implementation("com.russhwolf:multiplatform-settings:0.8.1")
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
                api("io.ktor:ktor-client-okhttp:${findProperty("version.ktor")}")
                implementation("io.ktor:ktor-client-android:${findProperty("version.ktor")}")
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
        //val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            //iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        //val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            //iosSimulatorArm64Test.dependsOn(this)
            dependencies {
                //Network
                implementation("io.ktor:ktor-client-ios:${findProperty("version.ktor")}")
            }
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