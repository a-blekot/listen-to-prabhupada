plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:database"))
                implementation(project(":common:settings"))
                implementation(project(":common:player-api"))
                implementation(project(":common:lectures-api"))
                implementation(project(":common:utils"))

                implementation(libs.decompose.decompose)
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.mvikotlin.extensions.coroutines)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.napier)
            }
        }
    }
}