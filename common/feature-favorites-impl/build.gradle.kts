plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.database)
                implementation(projects.common.settings)
                implementation(projects.common.favoritesApi)
                implementation(projects.common.favoritesImpl)
                implementation(projects.common.playerApi)
                implementation(projects.common.playerImpl)
                implementation(projects.common.featureFavoritesApi)
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