import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("base-kotlin-convention")
}

android {
    setCompileSdkVersion((findProperty("android.compileSdk") as String).toInt())
    defaultConfig {
        applicationId = "com.prabhupadalectures.android"

        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    composeOptions {
        kotlinCompilerExtensionVersion = findProperty("version.compose_compiler") as String
    }
}

dependencies {
    implementation(project(":common:database"))
    implementation(project(":common:feature-results-api"))
    implementation(project(":common:feature-favorites-api"))
    implementation(project(":common:feature-downloads-api"))
    implementation(project(":common:filters"))
    implementation(project(":common:favorites-api"))
    implementation(project(":common:downloads-api"))
    implementation(project(":common:lectures-api"))
    implementation(project(":common:lectures-impl"))
    implementation(project(":common:network-api"))
    implementation(project(":common:network-impl"))
    implementation(project(":common:player-api"))
    implementation(project(":common:player-impl"))
    implementation(project(":common:root"))
    implementation(project(":common:settings"))
    implementation(project(":common:utils"))

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.appcompat)
    //Compose Utils
    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.swiperefresh)
    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //Logger
    implementation(libs.napier.android)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)

    implementation(libs.bundles.mvikotlin.bndl)
    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extCompose)
}

detekt {
    version = "1.0.0"
    reports {
        xml {
            destination = file("$project.buildDir/reports/detekt/detekt.xml")
        }
    }
}

ktlint {
    android.set(true)
    version.set("0.46.1")
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
    outputToConsole.set(true)
}