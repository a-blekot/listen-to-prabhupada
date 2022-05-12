plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(libs.decompose.decompose)
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.mvikotlin.rx)
                implementation(libs.mvikotlin.extensions.coroutines)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
