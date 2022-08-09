plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.data)
                implementation(projects.common.database)
                implementation(projects.common.settings)
                implementation(projects.common.downloadsApi)
                implementation(projects.common.downloadsImpl)
                implementation(projects.common.playerApi)
                implementation(projects.common.playerImpl)
                implementation(projects.common.featureDownloadsApi)
                implementation(projects.common.networkApi)
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