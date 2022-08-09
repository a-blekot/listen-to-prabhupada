plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.data)
                implementation(projects.common.lecturesApi)
                implementation(projects.common.utils)

                implementation(libs.decompose.decompose)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}