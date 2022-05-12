import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("android-application-convention")
    id("kotlin-parcelize")
}

android {
    defaultConfig {
        applicationId = "com.prabhupadalectures.android"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":common:database"))
    implementation(project(":common:feature-results-api"))
    implementation(project(":common:filters"))
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
    version.set("0.41.0")
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
    outputToConsole.set(true)
}