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
                implementation(project(":common:favorites-api"))
                implementation(project(":common:favorites-impl"))
                implementation(project(":common:player-api"))
                implementation(project(":common:player-impl"))
                implementation(project(":common:feature-favorites-api"))
                implementation(project(":common:network-api"))
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