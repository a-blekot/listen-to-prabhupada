plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(projects.common.data)
                implementation(projects.common.database)

                implementation(libs.decompose.decompose)
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.mvikotlin.rx)
                implementation(libs.mvikotlin.extensions.coroutines)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.napier)
            }
        }
    }
}
