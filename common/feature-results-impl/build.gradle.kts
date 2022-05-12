plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:database"))
                implementation(project(":common:feature-results-api"))
                implementation(project(":common:lectures-api"))
                implementation(project(":common:lectures-impl"))
                implementation(project(":common:network-api"))
                implementation(project(":common:player-api"))
                implementation(project(":common:player-impl"))
                implementation(project(":common:settings"))
                implementation(project(":common:utils"))

                implementation(libs.decompose.decompose)
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.napier)
            }
        }
    }
}