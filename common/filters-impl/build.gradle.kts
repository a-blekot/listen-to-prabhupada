plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
    id("kotlin-parcelize")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.data)
                implementation(projects.common.database)
                implementation(projects.common.filtersApi)
                implementation(projects.common.networkApi)
                implementation(projects.common.settings)
                implementation(projects.common.utils)

                implementation(libs.decompose.decompose)
                implementation(libs.mvikotlin.mvikotlin)
                implementation(libs.mvikotlin.extensions.coroutines)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.napier)
            }
        }
    }
}